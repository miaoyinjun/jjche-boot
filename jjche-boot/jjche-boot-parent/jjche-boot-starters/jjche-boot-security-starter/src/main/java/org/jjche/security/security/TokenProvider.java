package org.jjche.security.security;

import cn.hutool.core.util.IdUtil;
import cn.hutool.log.StaticLog;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.jjche.core.util.RequestHolder;
import org.jjche.security.property.SecurityJwtProperties;
import org.jjche.security.property.SecurityProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.security.Key;

/**
 * <p>TokenProvider class.</p>
 *
 * @author /
 * @version 1.0.8-SNAPSHOT
 */
@Component
public class TokenProvider implements InitializingBean {

    /**
     * Constant <code>AUTHORITIES_KEY="auth"</code>
     */
    public static final String AUTHORITIES_KEY = "auth";
    @Autowired
    private SecurityProperties properties;
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
     * @param userTypeEnum a {@link UserTypeEnum} object.
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
     * 初步检测Token
     *
     * @param request /
     * @return /
     */
    public String resolveToken() {
        HttpServletRequest request = RequestHolder.getHttpServletRequest();
        SecurityJwtProperties securityJwtProperties = properties.getJwt();
        final String bearerToken = request.getHeader(securityJwtProperties.getHeader());
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(securityJwtProperties.getTokenStartWith())) {
            // 去掉令牌前缀
            return bearerToken.replace(securityJwtProperties.getTokenStartWith(), "");
        } else {
            StaticLog.debug("非法Token：{}", bearerToken);
        }
        return null;
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
}
