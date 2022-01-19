package org.jjche.core.exception;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.StaticLog;
import org.apache.commons.lang3.StringUtils;
import org.jjche.common.util.HttpUtil;
import org.jjche.common.util.ThrowableUtil;
import org.jjche.core.alarm.dd.AlarmDingTalkService;
import org.jjche.core.wrapper.response.ResultWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.ServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 统一异常处理
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-07-09
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    @Autowired
    private AlarmDingTalkService alarmDingTalkService;

    /**
     * <p>
     * 全局异常
     * </p>
     *
     * @param e       a {@link java.lang.Exception} object.
     * @param request a {@link javax.servlet.ServletRequest} object.
     * @return a {@link ResultWrapper} object.
     * @author miaoyj
     * @since 2020-07-09
     */
    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultWrapper exception(Throwable e, ServletRequest request) {
        BoxLog log = new BoxLog();
        try {
            if (request != null && request instanceof ContentCachingRequestWrapper) {
                ContentCachingRequestWrapper wrapper = (ContentCachingRequestWrapper) request;
                String ua = wrapper.getHeader(HttpHeaders.USER_AGENT);
                UserAgent userAgent = UserAgentUtil.parse(ua);
                log.setBrowser(HttpUtil.getBrowser(userAgent));
                log.setBrowser(HttpUtil.getOs(userAgent));
                log.setRequestIp(ServletUtil.getClientIP(wrapper));
                log.setRequestUri(wrapper.getRequestURI());
                log.setRequestMethod(wrapper.getMethod());
                log.setRequestParams(ServletUtil.getParamMap(wrapper));
                if (!ServletUtil.isMultipart(wrapper)) {
                    String bodyStr = StringUtils.toEncodedString(wrapper.getContentAsByteArray(), Charset.forName(wrapper.getCharacterEncoding()));
                    log.setRequestBody(bodyStr);
                }
                log.setRequestHeaders(ServletUtil.getHeaderMap(wrapper));
            }
        } catch (Exception ex) {

        } finally {
            log.setExceptionStack(ThrowableUtil.getStackTrace(e));
            StaticLog.error("全局异常信息 :{}", JSONUtil.toJsonPrettyStr(log));
            alarmDingTalkService.sendAlarm("全局异常");
        }
        return ResultWrapper.error();
    }

    /**
     * <p>
     * Feign调用ResultWrapper异常
     * </p>
     *
     * @param e 异常
     * @return 结果
     */
    @ExceptionHandler({FeignResultWrapperException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultWrapper feignResultWrapperException(Exception e) {
        String msg = e.getMessage();
        return JSONUtil.toBean(msg, ResultWrapper.class);
    }


    /**
     * <p>
     * 断言验证异常
     * </p>
     *
     * @param e a {@link java.lang.Exception} object.
     * @return a {@link ResultWrapper} object.
     * @author miaoyj
     * @since 2020-07-09
     */
    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultWrapper allException(Exception e) {
        return ResultWrapper.validError(e.getMessage());
    }

    /**
     * <p>
     * URL路径，默认参数验证，控制器@RequestParam(required = true)
     * </p>
     *
     * @param e a {@link java.lang.Exception} object.
     * @return a {@link ResultWrapper} object.
     * @author miaoyj
     * @since 2020-07-09
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultWrapper requestParamArgumentException(Exception e) {
        String fieldName = ((MissingServletRequestParameterException) e).getParameterName();
        String msg = StrUtil.format("{}: 不能为空", fieldName);
        return ResultWrapper.parameterError(msg);
    }

    /**
     * <p>
     * URL路径，自定义消息参数验证，控制器@NotBlank(message = "用户名不能为空")，...
     * </p>
     *
     * @param e a {@link javax.validation.ConstraintViolationException} object.
     * @return a {@link ResultWrapper} object.
     * @author miaoyj
     * @since 2020-07-09
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultWrapper requestParamValidatorArgumentException(ConstraintViolationException e) {
        String errorMessage = e.getMessage();
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        if (!CollectionUtils.isEmpty(constraintViolations)) {
            StringBuilder msgBuilder = new StringBuilder();
            for (ConstraintViolation constraintViolation : constraintViolations) {
                msgBuilder.append(constraintViolation.getMessage()).append(",");
                break;
            }
            errorMessage = msgBuilder.toString();
            if (errorMessage.length() > 1) {
                errorMessage = errorMessage.substring(0, errorMessage.length() - 1);
            }
        }
        return ResultWrapper.parameterError(errorMessage);
    }

    /**
     * <p>
     * POST @RequestBody参数验证
     * </p>
     *
     * @param e a {@link org.springframework.web.bind.MethodArgumentNotValidException} object.
     * @return a {@link ResultWrapper} object.
     * @author miaoyj
     * @since 2020-07-09
     */
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultWrapper resolveMethodArgumentNotValidException(Exception e) {
        String errorMessage = e.getMessage();
        List<ObjectError> objectErrors = new ArrayList<>();
        if (e instanceof MethodArgumentNotValidException) {
            objectErrors = ((MethodArgumentNotValidException) e).getBindingResult().getAllErrors();
        } else if (e instanceof BindException) {
            objectErrors = ((BindException) e).getBindingResult().getAllErrors();
        }
        if (!CollectionUtils.isEmpty(objectErrors)) {
            StringBuilder msgBuilder = new StringBuilder();
            for (ObjectError objectError : objectErrors) {
                msgBuilder.append(objectError.getDefaultMessage()).append(",");
                break;
            }
            errorMessage = msgBuilder.toString();
            if (errorMessage.length() > 1) {
                errorMessage = errorMessage.substring(0, errorMessage.length() - 1);
            }
        }
        return ResultWrapper.parameterError(errorMessage);
    }

    /**
     * <p>
     * 请求超时
     * </p>
     *
     * @param e a {@link java.lang.Exception} object.
     * @return a {@link ResultWrapper} object.
     * @author miaoyj
     * @since 2020-07-09
     */
    @ExceptionHandler({RequestTimeoutException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultWrapper requestTimeoutException(Exception e) {
        return ResultWrapper.requestTimeout();
    }

    /**
     * <p>
     * 签名错误
     * </p>
     *
     * @param e a {@link java.lang.Exception} object.
     * @return a {@link ResultWrapper} object.
     * @author miaoyj
     * @since 2020-07-09
     */
    @ExceptionHandler({SignException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultWrapper signException(Exception e) {
        return ResultWrapper.signError();
    }


    /**
     * <p>
     * 用户名或密码错误
     * </p>
     *
     * @param e a {@link java.lang.Exception} object.
     * @return a {@link ResultWrapper} object.
     * @author miaoyj
     * @since 2020-07-09
     */
    @ExceptionHandler({UsernameNotFoundException.class, BadCredentialsException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultWrapper userException(Exception e) {
        return ResultWrapper.userError();
    }

    /**
     * <p>
     * 账户已被禁用
     * </p>
     *
     * @param e a {@link java.lang.Exception} object.
     * @return a {@link ResultWrapper} object.
     * @author miaoyj
     * @since 2020-07-09
     */
    @ExceptionHandler({DisabledException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultWrapper userDisabledException(Exception e) {
        return ResultWrapper.userDisabledError();
    }

    /**
     * <p>
     * 账户被锁定
     * </p>
     *
     * @param e a {@link java.lang.Exception} object.
     * @return a {@link ResultWrapper} object.
     * @author miaoyj
     * @since 2020-07-09
     */
    @ExceptionHandler({LockedException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultWrapper userLockedException(Exception e) {
        return ResultWrapper.userLockedError();
    }

    /**
     * <p>
     * 账户过期
     * </p>
     *
     * @param e a {@link java.lang.Exception} object.
     * @return a {@link ResultWrapper} object.
     * @author miaoyj
     * @since 2020-07-09
     */
    @ExceptionHandler({AccountExpiredException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultWrapper usernameExpiredException(Exception e) {
        return ResultWrapper.userNameExpiredError();
    }

    /**
     * <p>
     * 密码过期
     * </p>
     *
     * @param e a {@link java.lang.Exception} object.
     * @return a {@link ResultWrapper} object.
     * @author miaoyj
     * @since 2020-07-09
     */
    @ExceptionHandler({CredentialsExpiredException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultWrapper userCredentialsExpiredException(Exception e) {
        return ResultWrapper.userCredentialsExpiredError();
    }

    /**
     * <p>
     * 获取授权信息错误
     * </p>
     *
     * @param e a {@link java.lang.Exception} object.
     * @return a {@link ResultWrapper} object.
     * @author miaoyj
     * @since 2020-07-09
     */
    @ExceptionHandler({AuthenticationTokenNotFoundException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResultWrapper authenticationTokenNotFoundException(Exception e) {
        return ResultWrapper.tokenNotFoundError();
    }

    /**
     * <p>authenticationTokenExpiredException.</p>
     *
     * @param e a {@link java.lang.Exception} object.
     * @return a {@link ResultWrapper} object.
     */
    @ExceptionHandler({AuthenticationTokenExpiredException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResultWrapper authenticationTokenExpiredException(Exception e) {
        return ResultWrapper.tokenExpiredError();
    }

    /**
     * <p>
     * 不允许访问
     * </p>
     *
     * @param e a {@link java.lang.Exception} object.
     * @return a {@link ResultWrapper} object.
     * @author miaoyj
     * @since 2020-07-09
     */
    @ExceptionHandler({AccessDeniedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResultWrapper authenticationAccessDeniedException(Exception e) {
        return ResultWrapper.userAccessDeniedError();
    }
}
