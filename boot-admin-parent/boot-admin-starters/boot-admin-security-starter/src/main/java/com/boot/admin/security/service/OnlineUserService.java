package com.boot.admin.security.service;

import cn.hutool.extra.servlet.ServletUtil;
import com.boot.admin.cache.service.RedisService;
import com.boot.admin.common.constant.CacheKey;
import com.boot.admin.common.util.FileUtil;
import com.boot.admin.common.util.HttpUtil;
import com.boot.admin.common.util.RsaUtils;
import com.boot.admin.common.util.StrUtil;
import com.boot.admin.core.util.SecurityUtils;
import com.boot.admin.security.dto.JwtUserDto;
import com.boot.admin.security.dto.OnlineUserDto;
import com.boot.admin.security.property.SecurityJwtProperties;
import com.boot.admin.security.property.SecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * <p>OnlineUserService class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2019年10月26日21:56:27
 */
@Service
@Slf4j
public class OnlineUserService {

    @Autowired
    private SecurityProperties properties;
    @Autowired
    private RedisService redisService;

    /**
     * 保存在线用户信息
     *
     * @param jwtUserDto /
     * @param token      /
     * @param request    /
     */
    public void save(JwtUserDto jwtUserDto, String token, HttpServletRequest request) {
        String dept = jwtUserDto.getUser().getDept().getName();
        String ip = ServletUtil.getClientIP(request);
        String browser = HttpUtil.getBrowser(request);
        String address = StrUtil.getCityInfo(ip);
        OnlineUserDto onlineUserDto = null;
        try {
            String publicKey = properties.getRsa().getPublicKey();
            String encryptToken = RsaUtils.encryptBypPublicKey(publicKey, token);
            String userAgent = request.getHeader(HttpHeaders.USER_AGENT);
            String os = HttpUtil.getUserAgent(request).getOs().getName();
            onlineUserDto = new OnlineUserDto(jwtUserDto.getUsername(),
                    jwtUserDto.getUser().getNickName(), dept, browser, userAgent, os, ip, address,
                    encryptToken, new Date(), new Date());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        SecurityJwtProperties securityJwtProperties = properties.getJwt();
        redisService.objectSetObject(securityJwtProperties.getOnlineKey() + token, onlineUserDto, securityJwtProperties.getTokenValidityInMilliSeconds());
    }

    /**
     * 查询全部数据，不分页
     *
     * @param filter /
     * @return /
     */
    public List<OnlineUserDto> getAll(String filter) {
        SecurityJwtProperties securityJwtProperties = properties.getJwt();
        Set<String> keys = redisService.keys(securityJwtProperties.getOnlineKey());
        List<OnlineUserDto> onlineUserDtos = new ArrayList<>();
        for (String key : keys) {
            OnlineUserDto onlineUserDto = redisService.objectGetObject(key, OnlineUserDto.class);
            if (StringUtils.isNotBlank(filter)) {
                if (onlineUserDto.toString().contains(filter)) {
                    onlineUserDtos.add(onlineUserDto);
                }
            } else {
                onlineUserDtos.add(onlineUserDto);
            }
        }
        onlineUserDtos.sort((o1, o2) -> o2.getLastAccessTime().compareTo(o1.getLastAccessTime()));
        return onlineUserDtos;
    }

    /**
     * 踢出用户
     *
     * @param token /
     */
    public void kickOut(String token) {
        SecurityJwtProperties securityJwtProperties = properties.getJwt();
        token = securityJwtProperties.getOnlineKey() + token;
        redisService.delete(token);
    }

    /**
     * 退出登录
     *
     * @param token /
     */
    public void logout(String token) {
        try {
            String username = SecurityUtils.getCurrentUsername();
            Long userId = SecurityUtils.getCurrentUserId();

            redisService.delete(CacheKey.DATE_USER_ID + userId);
            redisService.delete(CacheKey.MENU_USER_ID + userId);
            redisService.delete(CacheKey.ROLE_AUTH + userId);

            SecurityJwtProperties securityJwtProperties = properties.getJwt();
            String key = securityJwtProperties.getOnlineKey() + token;
            redisService.delete(key);
            redisService.delete(CacheKey.JWT_USER_NAME + username);
            redisService.delete(CacheKey.USER_NAME + username);
        } catch (Exception e) {

        }
    }

    /**
     * 导出
     *
     * @param all      /
     * @param response /
     * @throws java.io.IOException if any.
     */
    public void download(List<OnlineUserDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (OnlineUserDto user : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("用户名", user.getUserName());
            map.put("部门", user.getDept());
            map.put("登录IP", user.getIp());
            map.put("登录地点", user.getAddress());
            map.put("浏览器", user.getBrowser());
            map.put("登录日期", user.getLoginTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    /**
     * 查询用户
     *
     * @param key /
     * @return /
     */
    public OnlineUserDto getOne(String key) {
        return redisService.objectGetObject(key, OnlineUserDto.class);
    }

    /**
     * 检测用户是否在之前已经登录，已经登录踢下线
     *
     * @param userName   用户名
     * @param igoreToken a {@link java.lang.String} object.
     */
    public void checkLoginOnUser(String userName, String igoreToken) {
        List<OnlineUserDto> onlineUserDtos = getAll(userName);
        if (onlineUserDtos == null || onlineUserDtos.isEmpty()) {
            return;
        }
        for (OnlineUserDto onlineUserDto : onlineUserDtos) {
            if (onlineUserDto.getUserName().equals(userName)) {
                try {
                    String privateKey = properties.getRsa().getPrivateKey();
                    String token = RsaUtils.decryptByPrivateKey(privateKey, onlineUserDto.getKey());
                    if (StringUtils.isNotBlank(igoreToken) && !igoreToken.equals(token)) {
                        this.kickOut(token);
                    } else if (StringUtils.isBlank(igoreToken)) {
                        this.kickOut(token);
                    }
                } catch (Exception e) {
                    log.error("checkUser is error", e);
                }
            }
        }
    }

    /**
     * 根据用户名强退用户
     *
     * @param username /
     */
    @Async
    public void kickOutForUsername(String username) {
        List<OnlineUserDto> onlineUsers = getAll(username);
        String privateKey = properties.getRsa().getPrivateKey();
        for (OnlineUserDto onlineUser : onlineUsers) {
            if (onlineUser.getUserName().equals(username)) {
                // 解密Key
                String token = RsaUtils.decryptByPrivateKey(privateKey, onlineUser.getKey());
                kickOut(token);
            }
        }
    }
}
