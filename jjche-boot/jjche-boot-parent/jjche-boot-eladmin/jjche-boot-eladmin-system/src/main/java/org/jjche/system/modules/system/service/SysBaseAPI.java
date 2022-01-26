package org.jjche.system.modules.system.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.log.StaticLog;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.apache.commons.lang3.StringUtils;
import org.jjche.cache.service.RedisService;
import org.jjche.common.dto.OnlineUserDTO;
import org.jjche.common.system.api.ISysBaseAPI;
import org.jjche.common.util.FileUtil;
import org.jjche.common.util.HttpUtil;
import org.jjche.common.util.RsaUtils;
import org.jjche.common.util.StrUtil;
import org.jjche.core.util.SecurityUtils;
import org.jjche.security.auth.sms.SmsCodeAuthenticationToken;
import org.jjche.security.dto.JwtUserDto;
import org.jjche.security.property.SecurityJwtProperties;
import org.jjche.security.property.SecurityProperties;
import org.jjche.security.security.TokenProvider;
import org.jjche.security.security.UserTypeEnum;
import org.jjche.security.service.JwtUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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
public class SysBaseAPI implements ISysBaseAPI {

    @Autowired
    private SecurityProperties properties;
    @Autowired
    private RedisService redisService;
    @Autowired
    private JwtUserService jwtUserService;
    @Autowired
    private TokenProvider tokenProvider;

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
            onlineUserDto = new OnlineUserDTO(jwtUserDto.getUsername(), jwtUserDto.getUser().getNickName(), dept, browser, userAgent, os, ip, address, encryptToken, new Date(), new Date());
        } catch (Exception e) {
            StaticLog.error(e.getMessage(), e);
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
                    StaticLog.error("checkUser is error", e);
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
    public void logoutOnlineUser(String token) {
        String username = SecurityUtils.getCurrentUsername();
        SecurityJwtProperties securityJwtProperties = properties.getJwt();
        String key = securityJwtProperties.getOnlineKey() + token;
        redisService.delete(key);
        jwtUserService.removeByUserName(username);
    }

    @Override
    public Authentication getCheckAuthentication() {
        Authentication authentication = null;
        String token = tokenProvider.resolveToken();
        // 对于 Token 为空的不需要去查 Redis
        if (StrUtil.isNotBlank(token)) {
            OnlineUserDTO onlineUserDto = null;
            boolean cleanUserCache = false;
            try {
                onlineUserDto = this.getOnlineUser(token);
            } catch (ExpiredJwtException e) {
                StaticLog.error(e.getMessage());
                cleanUserCache = true;
            } finally {
                if (cleanUserCache || Objects.isNull(onlineUserDto)) {
                    this.logoutOnlineUser(token);
                }
            }
            if (onlineUserDto != null && org.springframework.util.StringUtils.hasText(token)) {
                authentication = this.getAuthentication(token, onlineUserDto);
            }
        }
        return authentication;
    }

    /**
     * <p>
     * 获取认证
     * </p>
     *
     * @param token         /
     * @param onlineUserDto /
     * @return /
     */
    private Authentication getAuthentication(String token, OnlineUserDTO onlineUserDto) {
        Authentication authentication = null;
        Claims claims = tokenProvider.getClaims(token);
        String userType = claims.getIssuer();
        String username = claims.getSubject();
        //密码
        if (cn.hutool.core.util.StrUtil.equals(UserTypeEnum.PWD.getValue(), userType)) {
            //这里会自动调用对应userDetailService的loadUserByUsername方法，但需要传密码
//            User principal = new User(claims.getSubject(), "******", new ArrayList<>());
//            authentication = new UsernamePasswordAuthenticationToken(principal, "");
            UserDetailsService userDetailsService = SpringUtil.getBean("userDetailsService");
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        }//短信
        else if (cn.hutool.core.util.StrUtil.equals(UserTypeEnum.SMS.getValue(), userType)) {
            authentication = new SmsCodeAuthenticationToken(username);
        }
        // Token 续期
        this.checkRenewal(token, onlineUserDto);
        return authentication;
    }

    /**
     * <p>
     * 检查token续期
     * </p>
     *
     * @param token         /
     * @param onlineUserDto /
     */
    private void checkRenewal(String token, OnlineUserDTO onlineUserDto) {
        SecurityJwtProperties securityJwtProperties = properties.getJwt();
        String tokenKey = securityJwtProperties.getOnlineKey() + token;
        long renew = securityJwtProperties.getTokenValidityInMilliSeconds();

        //更新最后访问时间，由于每次都要修改token缓存，所以判断是否续期token减少redis操作的目的就没意义了
        onlineUserDto.setLastAccessTime(DateUtil.date());
        redisService.objectSetObject(tokenKey, onlineUserDto, renew);

        /**
         // 判断是否续期token,计算token的过期时间
         long time = redisService.getExpire(tokenKey);
         Date expireDate = DateUtil.offset(new Date(), DateField.MILLISECOND, (int) time);
         // 判断当前时间与过期时间的时间差
         long differ = expireDate.getTime() - System.currentTimeMillis();
         // 如果在续期检查的范围内，则续期
         if (differ <= securityJwtProperties.getDetect()) {
         long renew = securityJwtProperties.getTokenValidityInMilliSeconds();
         redisService.setExpire(tokenKey, renew);
         }
         */
    }

    /**
     * <p>
     * 获取在线用户
     * </p>
     *
     * @param token /
     * @return /
     */
    private OnlineUserDTO getOnlineUser(String token) {
        SecurityJwtProperties securityJwtProperties = properties.getJwt();
        String tokenKey = securityJwtProperties.getOnlineKey() + token;
        return redisService.objectGetObject(tokenKey, OnlineUserDTO.class);
    }
}