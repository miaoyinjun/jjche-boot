package com.boot.admin.log.biz.starter.support.aop;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.StaticLog;
import com.boot.admin.common.pojo.AbstractResultWrapper;
import com.boot.admin.common.util.StrUtil;
import com.boot.admin.common.util.ThrowableUtil;
import com.boot.admin.core.util.RequestHolder;
import com.boot.admin.log.biz.beans.LogRecord;
import com.boot.admin.log.biz.context.LogRecordContext;
import com.boot.admin.log.biz.service.ILogRecordService;
import com.boot.admin.log.biz.service.IOperatorGetService;
import com.boot.admin.log.biz.starter.support.parse.LogRecordValueParser;
import eu.bitwalker.useragentutils.UserAgent;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpHeaders;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.*;

/**
 * <p>
 * 日志拦截器
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-04-30
 */
public class LogRecordInterceptor extends LogRecordValueParser implements InitializingBean, MethodInterceptor, Serializable {

    private LogRecordOperationSource logRecordOperationSource;

    private String tenantId;

    private ILogRecordService bizLogService;

    private IOperatorGetService operatorGetService;

    ThreadLocal<Long> currentTime = new ThreadLocal<>();

    /** {@inheritDoc} */
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        return execute(invocation, invocation.getThis(), method, invocation.getArguments());
    }

    /**
     * <p>
     * 记录
     * </p>
     *
     * @param invoker 调用
     * @param target  目的
     * @param method  方法
     * @param args    参数
     * @return 结果
     */
    private Object execute(MethodInvocation invoker, Object target, Method method, Object[] args) throws Throwable {
        currentTime.set(System.currentTimeMillis());
        Class<?> targetClass = getTargetClass(target);
        Object ret = null;
        boolean success = true;
        String errorMsg = "";
        Throwable throwable = null;
        LogRecord logRecord = null;
        try {
            LogRecordContext.clear();

            //执行前
            logRecord = getLogRecordOperationSource().computeLogRecordOperation(method, targetClass);
            //获取请求客户端信息
            setLogRecordHttpRequest(logRecord, args);

            String bizKey = logRecord.getBizKey();
            String bizNo = logRecord.getBizNo();
            String operatorId = logRecord.getOperatorId();
            String detail = logRecord.getDetail();
            String value = logRecord.getValue();
            //获取需要解析的表达式
            List<String> spElTemplates;
            String realOperator = "";
            if (StrUtil.isBlank(operatorId)) {
                spElTemplates = CollUtil.newArrayList(bizKey, bizNo, value, detail);
                if (operatorGetService.getUser() == null || StrUtil.isBlank(operatorGetService.getUser().getOperatorId())) {
                    throw new IllegalArgumentException("user is null");
                }
                realOperator = operatorGetService.getUser().getOperatorId();
            } else {
                spElTemplates = CollUtil.newArrayList(bizKey, bizNo, operatorId, value, detail);
            }
            Map<String, String> expressionValues = processTemplate(spElTemplates, ret, targetClass, method, args, errorMsg);
            logRecord.setBizKey(expressionValues.get(bizKey));
            logRecord.setBizNo(expressionValues.get(bizNo));
            logRecord.setOperator(StrUtil.isNotBlank(realOperator) ? realOperator : expressionValues.get(operatorId));
            logRecord.setValue(expressionValues.get(value));
            logRecord.setDetail(expressionValues.get(detail));

            //执行后
            ret = invoker.proceed();
        } catch (Exception e) {
            success = false;
            errorMsg = e.getMessage();
            throwable = e;
            logRecord.setExceptionDetail(ThrowableUtil.getStackTrace(e).getBytes());
            ;
        }
        try {
            if (ObjectUtil.isNotNull(logRecord)) {
                recordExecute(ret, method, args, logRecord, targetClass, success, errorMsg);
            }
        } catch (Exception t) {
            //记录日志错误不要影响业务
            StaticLog.error("log record parse exception:{}", ThrowableUtil.getStackTrace(t));
        } finally {
            LogRecordContext.clear();
        }
        if (throwable != null) {
            throw throwable;
        }
        return ret;
    }

    /**
     * <p>
     * 记录日志
     * </p>
     *
     * @param ret         结果
     * @param method      方法
     * @param args        参数
     * @param logRecord   日志对象
     * @param targetClass 目的类
     * @param success     是否成功
     * @param errorMsg    错误消息
     */
    private void recordExecute(Object ret, Method method, Object[] args, LogRecord logRecord,
                               Class<?> targetClass, boolean success, String errorMsg) {
        String methodName = targetClass.getName() + "." + method.getName() + "()";
        String result = "未提供";
        if (success) {
            if (ObjectUtil.isNotNull(ret)) {
                //是否控制器返回类型
                boolean isResultWrapper = ClassUtil.isAssignable(AbstractResultWrapper.class, ret.getClass());
                if (isResultWrapper) {
                    AbstractResultWrapper iResultWrapper = Convert.convert(AbstractResultWrapper.class, ret);
                    result = iResultWrapper.getMessage();
                } else {
                    result = StrUtil.toString(ret);
                }
            }
        } else {
            result = errorMsg;
        }

        try {
            logRecord.setSuccess(success);
            logRecord.setMethod(methodName);
            logRecord.setTenant(tenantId);
            logRecord.setResult(result);
            logRecord.setCreateTime(DateUtil.date().toTimestamp());

            //save log 需要新开事务，失败日志不能因为事务回滚而丢失
            if (bizLogService == null) {
                StaticLog.error("bizLogService not init!!");
            }
            logRecord.setTime(System.currentTimeMillis() - currentTime.get());
            currentTime.remove();
            bizLogService.record(logRecord);
        } catch (Exception t) {
            StaticLog.error("log record execute exception:{}", ThrowableUtil.getStackTrace(t));
        }
    }

    /**
     * <p>
     * 获取目的类
     * </p>
     *
     * @param target 对象
     * @return 目的类
     */
    private Class<?> getTargetClass(Object target) {
        return AopProxyUtils.ultimateTargetClass(target);
    }

    /**
     * <p>
     * 获取操作人
     * </p>
     *
     * @return 操作人
     */
    public LogRecordOperationSource getLogRecordOperationSource() {
        return logRecordOperationSource;
    }

    /**
     * <p>
     * 设置操作人
     * </p>
     *
     * @param logRecordOperationSource 操作人
     */
    public void setLogRecordOperationSource(LogRecordOperationSource logRecordOperationSource) {
        this.logRecordOperationSource = logRecordOperationSource;
    }

    /**
     * <p>
     * 设置租户
     * </p>
     *
     * @param tenant 租户
     */
    public void setTenant(String tenant) {
        this.tenantId = tenant;
    }

    /**
     * <p>
     * 设置日志记录回调类
     * </p>
     *
     * @param bizLogService 回调类
     */
    public void setLogRecordService(ILogRecordService bizLogService) {
        this.bizLogService = bizLogService;
    }

    /** {@inheritDoc} */
    @Override
    public void afterPropertiesSet() throws Exception {
        bizLogService = beanFactory.getBean(ILogRecordService.class);
        operatorGetService = beanFactory.getBean(IOperatorGetService.class);
        if (bizLogService == null) {
            StaticLog.error("bizLogService not null");
        }
    }

    /**
     * <p>
     * 获取操作人
     * </p>
     *
     * @param operatorGetService 操作人
     */
    public void setOperatorGetService(IOperatorGetService operatorGetService) {
        this.operatorGetService = operatorGetService;
    }

    /**
     * <p>
     * 获取请求客户端信息
     * </p>
     *
     * @param logRecord 日志
     * @param args      an array of {@link java.lang.Object} objects.
     */
    public void setLogRecordHttpRequest(LogRecord logRecord, Object[] args) {
        try {
            HttpServletRequest request = RequestHolder.getHttpServletRequest();
            String ip = StrUtil.getIp(request);
            String ua = request.getHeader(HttpHeaders.USER_AGENT);
            UserAgent userAgent = UserAgent.parseUserAgentString(ua);
            logRecord.setBrowser(StrUtil.getBrowser(userAgent));
            logRecord.setOs(StrUtil.getOs(userAgent));
            logRecord.setUserAgent(ua);
            logRecord.setRequestIp(ip);

            if (request instanceof ContentCachingRequestWrapper) {
                ContentCachingRequestWrapper wrapper = (ContentCachingRequestWrapper) request;
                logRecord.setUrl(wrapper.getRequestURI());
                if (!ServletUtil.isMultipart(wrapper) && logRecord.isSaveParams()) {
                    Map<String, Object> paramMap = new HashMap<>(2);
                    Map<String, String> paramsQueryMap = ServletUtil.getParamMap(wrapper);
                    //参数值
                    List<Object> argValues = new ArrayList<>(Arrays.asList(args));
                    if (CollUtil.isNotEmpty(argValues)) {
                        paramMap.put("body", argValues.get(0));
                    }
                    paramMap.put("query", paramsQueryMap);
                    String paramStr = JSONUtil.toJsonPrettyStr(paramMap);
                    logRecord.setParams(paramStr);
                }
            }
        } catch (Exception e) {
            StaticLog.error("setLogRecordHttpRequest:{}", ThrowableUtil.getStackTrace(e));
        }
    }
}
