package org.jjche.filter.encryption.api.interceptor;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.log.StaticLog;
import org.jjche.common.enums.FilterEncryptionEnum;
import org.jjche.core.exception.SignException;
import org.jjche.filter.property.FilterEncryptionApplicationProperties;
import org.jjche.filter.property.FilterEncryptionProperties;
import org.jjche.filter.util.EncryptionUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * MD5接口加密过滤器
 * </p>
 * <p>
 * sign:md5生成16进制小写字符串(ASCII排序URL参数(空参数不参与)+appKey+timestamp+nonce)
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-08-07
 */
public class EncryptionMd5Interceptor implements HandlerInterceptor {
    private FilterEncryptionProperties encryptionProperties;

    /**
     * <p>Constructor for EncryptionMd5Interceptor.</p>
     *
     * @param encryptionProperties a {@link FilterEncryptionProperties} object.
     */
    public EncryptionMd5Interceptor(FilterEncryptionProperties encryptionProperties) {
        this.encryptionProperties = encryptionProperties;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        String queryString = request.getQueryString();
        String appIdValue = request.getHeader(FilterEncryptionEnum.APP_ID.getKey());
        String timestampValue = request.getHeader(FilterEncryptionEnum.TIMESTAMP.getKey());
        String nonceValue = request.getHeader(FilterEncryptionEnum.NONCE.getKey());
        String signValue = request.getHeader(FilterEncryptionEnum.SIGN.getKey());

        StaticLog.info("md5Filter.queryString:{},appId:{},timestamp:{},nonce:{},sign:{}", queryString, appIdValue, timestampValue, nonceValue, signValue);

        /** 根据参数排序后拼接为字符串 */
        String queryOrderedString = EncryptionUtil.queryOrderedString(queryString);

        /** 校验appId有效性 参数*/
        String appKey = null;
        List<FilterEncryptionApplicationProperties> applications = encryptionProperties.getApplications();
        FilterEncryptionApplicationProperties applicationProperty = EncryptionUtil.getApplicationProperty(appIdValue, applications);
        if (ObjectUtil.isNotNull(applicationProperty)) {
            appKey = applicationProperty.getKey();
        }
        Assert.notNull(appKey, FilterEncryptionEnum.APP_ID.getErrMsg());

        /** md5生成16进制小写字符串 */
        String mySign = EncryptionUtil.md5Sign(queryOrderedString, appKey, timestampValue, nonceValue);
        if (ObjectUtil.notEqual(signValue, mySign)) {
            throw new SignException();
        }
        return true;
    }
}
