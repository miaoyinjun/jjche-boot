package com.boot.admin.core.wrapper.constant;

/**
 * <p>
 * httpStatus状态码定义
 * </p>
 *
 * @author miaoyj
 * @since 2020-09-09
 * @version 1.0.8-SNAPSHOT
 */
public interface HttpStatusConstant {

    /**
     * 服务内部错误 {@value}
     */
    String HTTP_INTERNAL_ERROR = "服务内部错误";

    /**
     * 验证错误 {@value}
     */
    String HTTP_BAD_REQUEST = "验证错误";

    /**
     * 授权过期，不允许访问 {@value}
     */
    String HTTP_FORBIDDEN = "授权过期，不允许访问";


    /**
     * 成功 {@value}
     */
    int CODE_OK = 2001;

    /**
     * 成功 {@value}
     */
    String MSG_OK = "OK";

    /**
     * 内部错误 {@value}
     */
    int CODE_UNKNOWN_ERROR = 5001;

    /**
     * 内部错误 {@value}
     */
    String MSG_UNKNOWN_ERROR = "系统未知错误,请反馈给管理员";

    /**
     * 参数错误 {@value}
     */
    int CODE_PARAMETER_ERROR = 4001;

    /**
     * 参数错误 {@value}
     */
    String MSG_PARAMETER_ERROR = "参数错误";

    /**
     * 业务验证错误 {@value}
     */
    int CODE_VALID_ERROR = 4002;

    /**
     * 业务验证错误 {@value}
     */
    String MSG_VALID_ERROR = "业务验证错误";

    /**
     * 请求超时 {@value}
     */
    int CODE_REQUEST_TIMEOUT = 4003;

    /**
     * 请求超时 {@value}
     */
    String MSG_REQUEST_TIMEOUT = "请求超时";

    /**
     * 签名错误 {@value}
     */
    int CODE_SIGN_ERROR = 4004;

    /**
     * 签名错误 {@value}
     */
    String MSG_SIGN_ERROR = "签名错误";

    /**
     * 请不要频繁操作 {@value}
     */
    int CODE_REPEAT_SUBMIT = 4005;

    /**
     * 请不要频繁操作 {@value}
     */
    String MSG_REPEAT_SUBMIT = "请不要频繁操作";

    /**
     * 未授权 {@value}
     */
    int CODE_TOKEN_ERROR = 4011;

    /**
     * 未授权 {@value}
     */
    String MSG_TOKEN_ERROR = "未授权或授权过期";

    /**
     * 授权过期 {@value}
     */
    int CODE_TOKEN_EXPIRED = 4012;

    /**
     * 授权过期 {@value}
     */
    String MSG_TOKEN_EXPIRED = "授权过期";

    /**
     * 不允许访问 {@value}
     */
    int CODE_USER_ACCESS_DENIED = 4031;

    /**
     * 不允许访问 {@value}
     */
    String MSG_USER_ACCESS_DENIED = "不允许访问";

    /**
     * 找不到认证信息 {@value}
     */
    int CODE_TOKEN_NOT_FOUND = 4013;

    /**
     * 找不到认证信息 {@value}
     */
    String MSG_TOKEN_NOT_FOUND = "找不到认证信息";

    /**
     * 用户名或密码错误 {@value}
     */
    int CODE_USERNAME_NOTFOUND_OR_BAD_CREDENTIALS = 4006;

    /**
     * 用户名或密码错误 {@value}
     */
    String MSG_USERNAME_NOTFOUND_OR_BAD_CREDENTIALS = "用户名或密码错误";

    /**
     * 账户已被禁用 {@value}
     */
    int CODE_USER_DISABLED = 4007;

    /**
     * 账户已被禁用 {@value}
     */
    String MSG_USER_DISABLED = "账户已被禁用，请联系管理员";

    /**
     * 账户已被锁定 {@value}
     */
    int CODE_USER_LOCKED = 4008;

    /**
     * 账户已被锁定 {@value}
     */
    String MSG_USER_LOCKED = "账户已被锁定，请联系管理员";

    /**
     * 账户已过期 {@value}
     */
    int CODE_USERNAME_EXPIRED = 4009;

    /**
     * 账户已过期 {@value}
     */
    String MSG_USERNAME_EXPIRED = "账户已过期，请联系管理员";

    /**
     * 密码已过期 {@value}
     */
    int CODE_USER_CREDENTIALS_EXPIRED = 4010;

    /**
     * 密码过期 {@value}
     */
    String MSG_USER_CREDENTIALS_EXPIRED = "密码已过期，请联系管理员";
}
