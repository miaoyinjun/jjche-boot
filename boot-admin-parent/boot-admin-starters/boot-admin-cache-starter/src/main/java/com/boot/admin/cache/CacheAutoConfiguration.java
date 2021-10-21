package com.boot.admin.cache;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import com.boot.admin.cache.conf.CustomPrefixKeyStringSerializer;
import com.boot.admin.cache.conf.RedisCacheConfig;
import com.boot.admin.cache.service.RedisServiceImpl;
import com.boot.admin.common.constant.PackageConstant;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * <p>
 * 入口
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-07-17
 */
@Configuration
@Import({RedisCacheConfig.class, CustomPrefixKeyStringSerializer.class, RedisServiceImpl.class})
@EnableCreateCacheAnnotation
@EnableMethodCache(basePackages = {PackageConstant.BASE_PATH})
public class CacheAutoConfiguration {}
