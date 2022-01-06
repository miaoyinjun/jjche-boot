package org.jjche.filter.encryption.api.interceptor;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import org.jjche.common.enums.FilterEncryptionEnum;
import org.jjche.core.exception.RequestTimeoutException;
import org.jjche.filter.property.FilterEncryptionProperties;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 检查参数
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-08-14
 */
public class EncryptionCheckParamInterceptor implements HandlerInterceptor {
    private FilterEncryptionProperties encryptionProperties;

    /**
     * <p>Constructor for EncryptionCheckParamInterceptor.</p>
     *
     * @param encryptionProperties a {@link FilterEncryptionProperties} object.
     */
    public EncryptionCheckParamInterceptor(FilterEncryptionProperties encryptionProperties) {
        this.encryptionProperties = encryptionProperties;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        String appIdValue = request.getHeader(FilterEncryptionEnum.APP_ID.getKey());
        String timestampValue = request.getHeader(FilterEncryptionEnum.TIMESTAMP.getKey());
        String nonceValue = request.getHeader(FilterEncryptionEnum.NONCE.getKey());
        String signValue = request.getHeader(FilterEncryptionEnum.SIGN.getKey());


        /** appId 参数*/
        Assert.notBlank(appIdValue, FilterEncryptionEnum.APP_ID.getErrMsg());
        /** timestamp 参数*/
        Assert.notBlank(timestampValue, FilterEncryptionEnum.TIMESTAMP.getErrMsg());
        /** nonce 参数*/
        Assert.notBlank(nonceValue, FilterEncryptionEnum.NONCE.getErrMsg());
        /** sign 参数*/
        Assert.notBlank(signValue, FilterEncryptionEnum.SIGN.getErrMsg());

        /** 校验随机数长度 参数*/
        Assert.isTrue(StrUtil.length(nonceValue) == encryptionProperties.getNonceLength(), FilterEncryptionEnum.NONCE.getErrMsg());

        /** 校验时间有效性 参数*/
        Long paramTimestamp = Long.valueOf(String.valueOf(timestampValue));
        long now = System.currentTimeMillis();
        boolean isExpireTime = paramTimestamp == null || now - paramTimestamp > encryptionProperties.getTimestamp() || paramTimestamp - now > 0L;
        if (isExpireTime) {
            throw new RequestTimeoutException();
        }
        return true;
    }
}
