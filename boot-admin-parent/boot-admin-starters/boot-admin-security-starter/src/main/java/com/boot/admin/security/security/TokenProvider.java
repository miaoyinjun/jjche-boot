package com.boot.admin.security.security;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.boot.admin.cache.service.RedisService;
import com.boot.admin.security.auth.sms.SmsCodeAuthenticationToken;
import com.boot.admin.security.dto.OnlineUserDto;
import com.boot.admin.security.property.SecurityJwtProperties;
import com.boot.admin.security.property.SecurityProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;

/**
 * <p>TokenProvider class.</p>
 *
 * @author /
 * @version 1.0.8-SNAPSHOT
 */
@Slf4j
@Component
public class TokenProvider implements InitializingBean {

    @Autowired
    private SecurityProperties properties;
    @Autowired
    private RedisService redisService;
    /**
     * Constant <code>AUTHORITIES_KEY="auth"</code>
     */
    public static final String AUTHORITIES_KEY = "auth";
    private JwtParser jwtParser;
    private JwtBuilder jwtBuilder;

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterPropertiesSet() {
        SecurityJwtProperties securityJwtProperties = properties.getJwt();
        byte[] keyBytes = Decoders.BASE64.decode(securityJwtProperties.getBase64Secret());
        Key key = Keys.hmacShaKeyFor(keyBytes);

        jwtParser = Jwts.parserBuilder()
                .setSigningKey(key)
                .build();
        jwtBuilder = Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS512);
    }

    /**
     * <p>createToken.</p>
     *
     * @param name         a {@link java.lang.String} object.
     * @param userTypeEnum a {@link com.boot.admin.security.security.UserTypeEnum} object.
     * @return a {@link java.lang.String} object.
     */
    public String createToken(String name, UserTypeEnum userTypeEnum) {
        return jwtBuilder
                // 加入ID确保生成的 Token 都不一致
                .setId(IdUtil.simpleUUID())
                .claim(AUTHORITIES_KEY, name)
                .setSubject(name)
                .setIssuer(userTypeEnum.getValue())
                .compact();
    }

    /**
     * 依据Token 获取鉴权信息
     *
     * @param token /
     * @return /
     */
    Authentication getAuthentication(String token) {
        Authentication authentication = null;
        Claims claims = getClaims(token);
        String userType = claims.getIssuer();
        String username = claims.getSubject();
        //密码
        if (StrUtil.equals(UserTypeEnum.PWD.getValue(), userType)) {
            //这里会自动调用对应userDetailService的loadUserByUsername方法，但需要传密码
//            User principal = new User(claims.getSubject(), "******", new ArrayList<>());
//            authentication = new UsernamePasswordAuthenticationToken(principal, "");
            UserDetailsService userDetailsService = SpringUtil.getBean("userDetailsService");
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        }//短信
        else if (StrUtil.equals(UserTypeEnum.SMS.getValue(), userType)) {
            authentication = new SmsCodeAuthenticationToken(username);
        }
        return authentication;
    }

    /**
     * <p>getClaims.</p>
     *
     * @param token a {@link java.lang.String} object.
     * @return a {@link io.jsonwebtoken.Claims} object.
     */
    public Claims getClaims(String token) {
        return jwtParser
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * <p>checkRenewal.</p>
     *
     * @param tokenKey 需要检查的tokenKey
     */
    public void checkRenewal(String tokenKey, OnlineUserDto onlineUserDto) {
        //更新最后访问时间
        onlineUserDto.setLastAccessTime(new Date());
        redisService.objectSetObject(tokenKey, onlineUserDto);
        // 判断是否续期token,计算token的过期时间
        SecurityJwtProperties securityJwtProperties = properties.getJwt();
        long time = redisService.getExpire(tokenKey);
        Date expireDate = DateUtil.offset(new Date(), DateField.MILLISECOND, (int) time);
        // 判断当前时间与过期时间的时间差
        long differ = expireDate.getTime() - System.currentTimeMillis();
        // 如果在续期检查的范围内，则续期
        if (differ <= securityJwtProperties.getDetect()) {
            long renew = securityJwtProperties.getTokenValidityInMilliSeconds();
            redisService.setExpire(tokenKey, renew);
        }
    }

    /**
     * <p>getToken.</p>
     *
     * @param request a {@link javax.servlet.http.HttpServletRequest} object.
     * @return a {@link java.lang.String} object.
     */
    public String getToken(HttpServletRequest request) {
        SecurityJwtProperties securityJwtProperties = properties.getJwt();
        final String requestHeader = request.getHeader(securityJwtProperties.getHeader());
        if (requestHeader != null && requestHeader.startsWith(securityJwtProperties.getTokenStartWith())) {
            return requestHeader.substring(7);
        }
        return null;
    }
}
