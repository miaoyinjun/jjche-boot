package org.jjche.filter.encryption.limit;

import cn.hutool.core.lang.Assert;
import cn.hutool.log.StaticLog;
import com.google.common.collect.ImmutableList;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.jjche.common.annotation.Limit;
import org.jjche.common.enums.LimitType;
import org.jjche.common.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * <p>LimitAspect class.</p>
 *
 * @author /
 * @version 1.0.8-SNAPSHOT
 */
@Aspect
@Component
public class LimitAspect {

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    /**
     * <p>pointcut.</p>
     */
    @Pointcut("@annotation(org.jjche.common.annotation.Limit)")
    public void pointcut() {
    }

    /**
     * <p>around.</p>
     *
     * @param joinPoint a {@link org.aspectj.lang.ProceedingJoinPoint} object.
     * @return a {@link java.lang.Object} object.
     * @throws java.lang.Throwable if any.
     */
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method signatureMethod = signature.getMethod();
        Limit limit = signatureMethod.getAnnotation(Limit.class);
        LimitType limitType = limit.limitType();
        String key = limit.key();
        if (StrUtil.isEmpty(key)) {
            if (limitType == LimitType.IP) {
                key = StrUtil.getIp(request);
            } else {
                key = signatureMethod.getName();
            }
        }

        ImmutableList<Object> keys = ImmutableList.of(StrUtil.join(limit.prefix(), "_", key, "_", request.getRequestURI().replaceAll("/", "_")));

        String luaScript = buildLuaScript();
        RedisScript<Long> redisScript = new DefaultRedisScript<>(luaScript, Long.class);
        Number count = redisTemplate.execute(redisScript, keys, limit.count(), limit.period());
        Boolean isMorThan = null != count && count.intValue() <= limit.count();
        Assert.isTrue(isMorThan, "访问次数受限制");
        StaticLog.debug("第{}次访问key为 {}，描述为 [{}] 的接口", count, keys, limit.name());
        return joinPoint.proceed();
    }

    /**
     * 限流脚本
     */
    private String buildLuaScript() {
        return "local c" +
                "\nc = redis.call('get',KEYS[1])" +
                "\nif c and tonumber(c) > tonumber(ARGV[1]) then" +
                "\nreturn c;" +
                "\nend" +
                "\nc = redis.call('incr',KEYS[1])" +
                "\nif tonumber(c) == 1 then" +
                "\nredis.call('expire',KEYS[1],ARGV[2])" +
                "\nend" +
                "\nreturn c;";
    }
}
