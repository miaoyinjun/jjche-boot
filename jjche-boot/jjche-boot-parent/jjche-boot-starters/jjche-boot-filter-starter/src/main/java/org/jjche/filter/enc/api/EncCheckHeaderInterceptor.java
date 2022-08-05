package org.jjche.filter.enc.api;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.StaticLog;
import org.jjche.common.constant.FilterEncConstant;
import org.jjche.common.enums.FilterEncEnum;
import org.jjche.core.exception.RequestTimeoutException;
import org.jjche.core.exception.SignException;
import org.jjche.filter.util.EncUtil;
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
public class EncCheckHeaderInterceptor implements HandlerInterceptor {
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String appIdValue = request.getHeader(FilterEncEnum.APP_ID.getKey());
        String timestampValue = request.getHeader(FilterEncEnum.TIMESTAMP.getKey());
        String nonceValue = request.getHeader(FilterEncEnum.NONCE.getKey());
        String signValue = request.getHeader(FilterEncEnum.SIGN.getKey());

        /** appId */
        Assert.notBlank(appIdValue, FilterEncEnum.APP_ID.getErrMsg());
        /** timestamp */
        Assert.notBlank(timestampValue, FilterEncEnum.TIMESTAMP.getErrMsg());
        /** nonce */
        Assert.notBlank(nonceValue, FilterEncEnum.NONCE.getErrMsg());
        /** sign */
        Assert.notBlank(signValue, FilterEncEnum.SIGN.getErrMsg());

        /** 校验随机数长度 */
        Assert.isTrue(StrUtil.length(nonceValue) == FilterEncConstant.NONCE_LENGTH, FilterEncEnum.NONCE.getErrMsg());

        /** 校验时间有效性 */
        Long paramTimestamp = null;
        boolean isNumber = NumberUtil.isNumber(timestampValue);
        if (isNumber) {
            paramTimestamp = Long.valueOf(timestampValue);
        }
        long now = System.currentTimeMillis();
        boolean isExpireTime = paramTimestamp == null || now - paramTimestamp > FilterEncConstant.EXPIRE_TIME || paramTimestamp - now > 0L;
        if (isExpireTime) {
            throw new RequestTimeoutException();
        }

        StaticLog.info("md5Filter.appId:{},timestamp:{}, nonce:{},sign:{}", appIdValue, timestampValue, nonceValue, signValue);

        /** 校验appId有效性 */
        String appSecret = FilterEncConstant.DEFAULT_APP_SECRET;
        //Assert.notNull(appSecret, FilterEncEnum.APP_ID.getErrMsg());

        /** md5生成16进制小写字符串 */
        String mySign = EncUtil.md5Sign(appSecret, timestampValue, nonceValue);
        if (ObjectUtil.notEqual(signValue, mySign)) {
            throw new SignException();
        }
        return true;
    }
}
