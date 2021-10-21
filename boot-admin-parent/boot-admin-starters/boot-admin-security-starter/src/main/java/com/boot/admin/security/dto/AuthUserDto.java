package com.boot.admin.security.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * <p>AuthUserDto class.</p>
 *
 * @author Zheng Jie
 * @since 2018-11-30
 * @version 1.0.8-SNAPSHOT
 */
@Getter
@Setter
public class AuthUserDto {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    private String code;

    private String uuid = "";

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "{username=" + username + ", password= ******}";
    }
}
