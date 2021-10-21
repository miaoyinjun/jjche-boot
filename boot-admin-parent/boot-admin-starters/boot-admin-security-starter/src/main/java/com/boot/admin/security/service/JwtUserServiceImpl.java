package com.boot.admin.security.service;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.boot.admin.common.constant.CacheKey;
import com.boot.admin.security.dto.JwtUserDto;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 登录认证
 * </p>
 *
 * @author miaoyj
 * @since 2020-11-02
 * @version 1.0.8-SNAPSHOT
 */
@Service
public class JwtUserServiceImpl implements JwtUserService {
    @CreateCache(name = CacheKey.JWT_USER_NAME)
    private Cache<String, JwtUserDto> jwtUserCache;

    /** {@inheritDoc} */
    @Override
    public JwtUserDto getByUserName(String userName) {
        return jwtUserCache.get(userName);
    }

    /** {@inheritDoc} */
    @Override
    public void putByUserName(String userName, JwtUserDto jwtUserDto) {
        jwtUserCache.put(userName, jwtUserDto);
    }

    /** {@inheritDoc} */
    @Override
    public void removeByUserName(String userName) {
        jwtUserCache.remove(userName);
    }
}
