package org.jjche.common.wrapper.enums;

import org.jjche.common.wrapper.constant.HttpStatusConstant;

/**
 * <p>
 * api返回code定义
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-08-10
 */
public enum RCodeEnum {
    /**
     * OK
     */
    SUCCESS(HttpStatusConstant.CODE_OK, HttpStatusConstant.MSG_OK),
    /**
     * 内部错误
     */
    UNKNOWN_ERROR(HttpStatusConstant.CODE_UNKNOWN_ERROR, HttpStatusConstant.MSG_UNKNOWN_ERROR),
    /**
     * 参数错误
     */
    PARAMETER_ERROR(HttpStatusConstant.CODE_PARAMETER_ERROR, HttpStatusConstant.MSG_PARAMETER_ERROR),
    /**
     * 业务验证错误
     */
    VALID_ERROR(HttpStatusConstant.CODE_VALID_ERROR, HttpStatusConstant.MSG_VALID_ERROR),
    /**
     * 请求超时
     */
    REQUEST_TIMEOUT(HttpStatusConstant.CODE_REQUEST_TIMEOUT, HttpStatusConstant.MSG_REQUEST_TIMEOUT),
    /**
     * 签名错误
     */
    SIGN_ERROR(HttpStatusConstant.CODE_SIGN_ERROR, HttpStatusConstant.MSG_SIGN_ERROR),
    /**
     * 请不要频繁操作
     */
    REQUEST_LIMIT(HttpStatusConstant.CODE_REQUEST_LIMIT, HttpStatusConstant.MSG_REQUEST_LIMIT),
    /**
     * 未授权
     */
    TOKEN_ERROR(HttpStatusConstant.CODE_TOKEN_ERROR, HttpStatusConstant.MSG_TOKEN_ERROR),
    /**
     * 用户名或密码错误
     */
    USERNAME_NOT_FOUND_OR_BAD_CREDENTIALS(HttpStatusConstant.CODE_USERNAME_NOTFOUND_OR_BAD_CREDENTIALS, HttpStatusConstant.MSG_USERNAME_NOTFOUND_OR_BAD_CREDENTIALS),
    /**
     * 账户已被禁用
     */
    USER_DISABLED(HttpStatusConstant.CODE_USER_DISABLED, HttpStatusConstant.MSG_USER_DISABLED),
    /**
     * 账户被锁定
     */
    USER_LOCKED(HttpStatusConstant.CODE_USER_LOCKED, HttpStatusConstant.MSG_USER_LOCKED),
    /**
     * 账户过期
     */
    USERNAME_EXPIRED(HttpStatusConstant.CODE_USERNAME_EXPIRED, HttpStatusConstant.MSG_USERNAME_EXPIRED),
    /**
     * 密码过期
     */
    USER_CREDENTIALS_EXPIRED(HttpStatusConstant.CODE_USER_CREDENTIALS_EXPIRED, HttpStatusConstant.MSG_USER_CREDENTIALS_EXPIRED),
    /**
     * 授权过期
     */
    TOKEN_EXPIRED(HttpStatusConstant.CODE_TOKEN_EXPIRED, HttpStatusConstant.MSG_TOKEN_EXPIRED),
    /**
     * 不允许访问
     */
    USER_ACCESS_DENIED(HttpStatusConstant.CODE_USER_ACCESS_DENIED, HttpStatusConstant.MSG_USER_ACCESS_DENIED),
    /**
     * 找不到认证信息
     */
    TOKEN_NOT_FOUND(HttpStatusConstant.CODE_TOKEN_NOT_FOUND, HttpStatusConstant.MSG_TOKEN_NOT_FOUND),
    /**
     * 白名单限制
     */
    WHITE_IP(HttpStatusConstant.CODE_WHITE_IP, HttpStatusConstant.MSG_CODE_WHITE_IP),
    ;
    /**
     * 代码
     */
    private int code;

    /**
     * 结果
     */
    private String msg;

    RCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * <p>Getter for the field <code>code</code>.</p>
     *
     * @return a int.
     */
    public int getCode() {
        return code;
    }

    /**
     * <p>Getter for the field <code>msg</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getMsg() {
        return msg;
    }
}
