package com.boot.admin.core.wrapper.response;

import com.boot.admin.common.pojo.AbstractResultWrapper;
import com.boot.admin.core.wrapper.constant.HttpStatusConstant;
import com.boot.admin.core.wrapper.enums.ResultWrapperCodeEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * <p>
 * response包装操作类
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-07-09
 */
@Data
public class ResultWrapper<T> extends AbstractResultWrapper implements Serializable {

    /**
     * 编号
     */
    @ApiModelProperty(value = HttpStatusConstant.CODE_OK
            + ":" + HttpStatusConstant.MSG_OK + ";",
            example = HttpStatusConstant.CODE_OK + "")
    private int code;

    /**
     * 信息
     */
    @ApiModelProperty(example = HttpStatusConstant.MSG_OK + "")
    private String message;

    /**
     * 结果数据
     */
    private T data;

    /**
     * Instantiates a new wrapper. default code=200
     */
    public ResultWrapper() {
        this(ResultWrapperCodeEnum.SUCCESS.getCode(), ResultWrapperCodeEnum.SUCCESS.getMsg());
    }

    /**
     * Instantiates a new wrapper.
     *
     * @param code    the code
     * @param message the message
     */
    ResultWrapper(int code, String message) {
        this(code, message, null);
    }

    /**
     * Instantiates a new wrapper.
     *
     * @param code    the code
     * @param message the message
     * @param result  the result
     */
    ResultWrapper(int code, String message, T result) {
        super();
        this.code(code).message(message).data(result);
    }

    /**
     * Sets the 编号 , 返回自身的引用.
     *
     * @param code the new 编号
     * @return the wrapper
     */
    private ResultWrapper<T> code(int code) {
        this.setCode(code);
        return this;
    }

    /**
     * Sets the 信息 , 返回自身的引用.
     *
     * @param message the new 信息
     * @return the wrapper
     */
    private ResultWrapper<T> message(String message) {
        this.setMessage(message);
        return this;
    }

    /**
     * Sets the 结果数据 , 返回自身的引用.
     *
     * @param data the new 结果数据
     * @return the wrapper
     */
    public ResultWrapper<T> data(T data) {
        this.setData(data);
        return this;
    }

    /**
     * 判断是否成功： 依据 ResultWrapper.SUCCESS_CODE == this.code
     *
     * @return code =200,true;否则 false.
     */
    @JsonIgnore
    public boolean success() {
        return ResultWrapperCodeEnum.SUCCESS.getCode() == this.code;
    }

    /**
     * Wrap.
     *
     * @param <E>      the element type
     * @param codeEnum the codeEnum
     * @return the wrapper
     */
    private static <E> ResultWrapper<E> wrap(ResultWrapperCodeEnum codeEnum) {
        return new ResultWrapper<>(codeEnum.getCode(), codeEnum.getMsg(), null);
    }

    /**
     * Wrap.
     *
     * @param <E>      the element type
     * @param codeEnum the code enum
     * @param message  the message
     * @return the wrapper
     */
    private static <E> ResultWrapper<E> wrap(ResultWrapperCodeEnum codeEnum, String message) {
        return new ResultWrapper<>(codeEnum.getCode(), message, null);
    }

    /**
     * <p>
     * 内部错误
     * </p>
     *
     * @param <E> a E object.
     * @return 详细
     * @author miaoyj
     * @since 2020-08-10
     */
    public static <E> ResultWrapper<E> error() {
        return wrap(ResultWrapperCodeEnum.UNKNOWN_ERROR);
    }

    /**
     * <p>
     * 业务验证错误
     * </p>
     *
     * @param msg 错误信息
     * @param <E> a E object.
     * @return 详细
     * @author miaoyj
     * @since 2020-08-10
     */
    public static <E> ResultWrapper<E> validError(String msg) {
        return wrap(ResultWrapperCodeEnum.VALID_ERROR, msg);
    }

    /**
     * <p>
     * 参数错误
     * </p>
     *
     * @param msg 错误信息
     * @param <E> a E object.
     * @return 详细
     * @author miaoyj
     * @since 2020-08-10
     */
    public static <E> ResultWrapper<E> parameterError(String msg) {
        return wrap(ResultWrapperCodeEnum.PARAMETER_ERROR, msg);
    }

    /**
     * <p>
     * 请求超时
     * </p>
     *
     * @param <E> a E object.
     * @return 详情
     * @author miaoyj
     * @since 2020-08-10
     */
    public static <E> ResultWrapper<E> requestTimeout() {
        return wrap(ResultWrapperCodeEnum.REQUEST_TIMEOUT);
    }

    /**
     * <p>
     * 签名错误
     * </p>
     *
     * @param <E> a E object.
     * @return 详情
     * @author miaoyj
     * @since 2020-08-10
     */
    public static <E> ResultWrapper<E> signError() {
        return wrap(ResultWrapperCodeEnum.SIGN_ERROR);
    }

    /**
     * <p>
     * 不要频繁操作
     * </p>
     *
     * @param <E> a E object.
     * @return 详情
     * @author miaoyj
     * @since 2020-08-10
     */
    public static <E> ResultWrapper<E> repeatSubmit() {
        return wrap(ResultWrapperCodeEnum.REPEAT_SUBMIT);
    }


    /**
     * <p>
     * 成功
     * </p>
     *
     * @param <E> a E object.
     * @return 详情
     * @author miaoyj
     * @since 2020-08-10
     */
    public static <E> ResultWrapper<E> ok() {
        return new ResultWrapper<>();
    }

    /**
     * <p>
     * 成功
     * </p>
     *
     * @param o   对象
     * @param <E> a E object.
     * @return 详细
     * @author miaoyj
     * @since 2020-08-10
     */
    public static <E> ResultWrapper<E> ok(E o) {
        return new ResultWrapper<>(ResultWrapperCodeEnum.SUCCESS.getCode(), ResultWrapperCodeEnum.SUCCESS.getMsg(), o);
    }

    /**
     * <p>
     * 用户名或密码错误
     * </p>
     *
     * @param <E> a E object.
     * @return 详情
     * @author miaoyj
     * @since 2020-08-10
     */
    public static <E> ResultWrapper<E> userError() {
        return wrap(ResultWrapperCodeEnum.USERNAME_NOT_FOUND_OR_BAD_CREDENTIALS);
    }

    /**
     * <p>
     * 账户已被禁用
     * </p>
     *
     * @param <E> a E object.
     * @return 详情
     * @author miaoyj
     * @since 2020-08-10
     */
    public static <E> ResultWrapper<E> userDisabledError() {
        return wrap(ResultWrapperCodeEnum.USER_DISABLED);
    }

    /**
     * <p>
     * 账户被锁定
     * </p>
     *
     * @param <E> a E object.
     * @return 详情
     * @author miaoyj
     * @since 2020-08-10
     */
    public static <E> ResultWrapper<E> userLockedError() {
        return wrap(ResultWrapperCodeEnum.USER_LOCKED);
    }

    /**
     * <p>
     * 账户过期
     * </p>
     *
     * @param <E> a E object.
     * @return 详情
     * @author miaoyj
     * @since 2020-08-10
     */
    public static <E> ResultWrapper<E> userNameExpiredError() {
        return wrap(ResultWrapperCodeEnum.USERNAME_EXPIRED);
    }

    /**
     * <p>
     * 密码过期
     * </p>
     *
     * @param <E> a E object.
     * @return 详情
     * @author miaoyj
     * @since 2020-08-10
     */
    public static <E> ResultWrapper<E> userCredentialsExpiredError() {
        return wrap(ResultWrapperCodeEnum.USER_CREDENTIALS_EXPIRED);
    }

    /**
     * <p>
     * 授权过期
     * </p>
     *
     * @param <E> a E object.
     * @return 详情
     * @author miaoyj
     * @since 2020-08-10
     */
    public static <E> ResultWrapper<E> tokenExpiredError() {
        return wrap(ResultWrapperCodeEnum.TOKEN_EXPIRED);
    }

    /**
     * <p>
     * 未授权，token为空，token无法解析
     * </p>
     *
     * @param <E> a E object.
     * @return 详情
     * @author miaoyj
     * @since 2020-08-10
     */
    public static <E> ResultWrapper<E> tokenError() {
        return wrap(ResultWrapperCodeEnum.TOKEN_ERROR);
    }

    /**
     * <p>
     * 不允许访问
     * </p>
     *
     * @param <E> a E object.
     * @return 详情
     * @author miaoyj
     * @since 2020-08-10
     */
    public static <E> ResultWrapper<E> userAccessDeniedError() {
        return wrap(ResultWrapperCodeEnum.USER_ACCESS_DENIED);
    }

    /**
     * <p>
     * 找不到认证信息
     * </p>
     *
     * @return /
     * @param <E> a E object.
     */
    public static <E> ResultWrapper<E> tokenNotFoundError() {
        return wrap(ResultWrapperCodeEnum.TOKEN_NOT_FOUND);
    }
}
