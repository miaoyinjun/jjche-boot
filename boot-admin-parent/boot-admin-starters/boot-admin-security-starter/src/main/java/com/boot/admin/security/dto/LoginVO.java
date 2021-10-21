package com.boot.admin.security.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 登录出参
 * </p>
 *
 * @author miaoyj
 * @since 2020-09-21
 * @version 1.0.8-SNAPSHOT
 */
@Data
@AllArgsConstructor
public class LoginVO implements Serializable {
    private String token;
    JwtUserDto user;
}
