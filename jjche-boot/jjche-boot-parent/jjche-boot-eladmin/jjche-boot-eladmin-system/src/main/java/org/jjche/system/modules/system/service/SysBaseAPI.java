package org.jjche.system.modules.system.service;

import cn.hutool.extra.servlet.ServletUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jjche.cache.service.RedisService;
import org.jjche.common.dto.OnlineUserDTO;
import org.jjche.common.system.api.ISysBaseAPI;
import org.jjche.common.util.FileUtil;
import org.jjche.common.util.HttpUtil;
import org.jjche.common.util.RsaUtils;
import org.jjche.common.util.StrUtil;
import org.jjche.core.util.SecurityUtils;
import org.jjche.security.dto.JwtUserDto;
import org.jjche.security.property.SecurityJwtProperties;
import org.jjche.security.property.SecurityProperties;
import org.jjche.security.service.JwtUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * <p>
 * 本地操作
 * </p>
 *
 * @author miaoyj
 * @since 2022-01-25
 */
@Service
@Slf4j
public class SysBaseAPI implements ISysBaseAPI {

    @Autowired
    private SecurityProperties properties;
    @Autowired
    private RedisService redisService;
    @Autowired
    private JwtUserService jwtUserService;

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
        OnlineUserDTO onlineUserDto = null;
        try {
            String publicKey = properties.getRsa().getPublicKey();
            String encryptToken = RsaUtils.encryptBypPublicKey(publicKey, token);
            String userAgent = request.getHeader(HttpHeaders.USER_AGENT);
            String os = HttpUtil.getUserAgent(request).getOs().getName();
            onlineUserDto = new OnlineUserDTO(jwtUserDto.getUsername(),
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
    public List<OnlineUserDTO> getAll(String filter) {
        SecurityJwtProperties securityJwtProperties = properties.getJwt();
        Set<String> keys = redisService.keys(securityJwtProperties.getOnlineKey());
        List<OnlineUserDTO> onlineUserDTOS = new ArrayList<>();
        for (String key : keys) {
            OnlineUserDTO onlineUserDto = redisService.objectGetObject(key, OnlineUserDTO.class);
            if (StringUtils.isNotBlank(filter)) {
                if (onlineUserDto.toString().contains(filter)) {
                    onlineUserDTOS.add(onlineUserDto);
                }
            } else {
                onlineUserDTOS.add(onlineUserDto);
            }
        }
        onlineUserDTOS.sort((o1, o2) -> o2.getLastAccessTime().compareTo(o1.getLastAccessTime()));
        return onlineUserDTOS;
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
     * 导出
     *
     * @param all      /
     * @param response /
     * @throws java.io.IOException if any.
     */
    public void download(List<OnlineUserDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (OnlineUserDTO user : all) {
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
     * 检测用户是否在之前已经登录，已经登录踢下线
     *
     * @param userName   用户名
     * @param igoreToken a {@link java.lang.String} object.
     */
    public void checkLoginOnUser(String userName, String igoreToken) {
        List<OnlineUserDTO> onlineUserDTOS = getAll(userName);
        if (onlineUserDTOS == null || onlineUserDTOS.isEmpty()) {
            return;
        }
        for (OnlineUserDTO onlineUserDto : onlineUserDTOS) {
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
        List<OnlineUserDTO> onlineUsers = getAll(username);
        String privateKey = properties.getRsa().getPrivateKey();
        for (OnlineUserDTO onlineUser : onlineUsers) {
            if (onlineUser.getUserName().equals(username)) {
                // 解密Key
                String token = RsaUtils.decryptByPrivateKey(privateKey, onlineUser.getKey());
                kickOut(token);
            }
        }
    }

    @Override
    public OnlineUserDTO getOnlineUser(String tokenKey) {
        return redisService.objectGetObject(tokenKey, OnlineUserDTO.class);
    }

    @Override
    public void logoutOnlineUser(String token) {
        String username = SecurityUtils.getCurrentUsername();
        SecurityJwtProperties securityJwtProperties = properties.getJwt();
        String key = securityJwtProperties.getOnlineKey() + token;
        redisService.delete(key);
        jwtUserService.removeByUserName(username);
    }
}