package org.jjche.security.service;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import org.jjche.cache.service.RedisService;
import org.jjche.common.constant.CacheKey;
import org.jjche.common.dto.JwtUserDto;
import org.jjche.common.dto.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 登录认证
 * </p>
 *
 * @author miaoyj
 * @version 1.0.8-SNAPSHOT
 * @since 2020-11-02
 */
@Service
public class JwtUserServiceImpl implements JwtUserService {
    @CreateCache(name = CacheKey.JWT_USER_NAME)
    private Cache<String, JwtUserDto> jwtUserCache;
    @Autowired
    private RedisService redisService;

    /**
     * {@inheritDoc}
     */
    @Override
    public JwtUserDto getByUserName(String userName) {
        return jwtUserCache.get(userName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void putByUserName(String userName, JwtUserDto jwtUserDto) {
        jwtUserCache.put(userName, jwtUserDto);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeByUserName(String userName) {
        JwtUserDto jwtUserDto = this.getByUserName(userName);
        if (jwtUserDto != null) {
            UserVO user = jwtUserDto.getUser();
            if (user != null) {
                Long userId = user.getId();
                jwtUserCache.remove(userName);
                redisService.delete(CacheKey.MENU_USER_ID + userId);
                redisService.delete(CacheKey.PERMISSION_DATA_RULE_USER_ID + userId);
                redisService.delete(CacheKey.PERMISSION_DATA_FIELD_USER_ID + userId);
            }
        }

    }
}
