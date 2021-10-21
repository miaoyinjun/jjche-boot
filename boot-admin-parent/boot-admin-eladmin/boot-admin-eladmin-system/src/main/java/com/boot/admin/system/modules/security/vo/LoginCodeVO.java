package com.boot.admin.system.modules.security.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 登录验证码出参
 * </p>
 *
 * @author miaoyj
 * @since 2020-09-21
 * @version 1.0.8-SNAPSHOT
 */
@Data
@AllArgsConstructor
public class LoginCodeVO implements Serializable {
    private String img;
    private String uuid;
}
