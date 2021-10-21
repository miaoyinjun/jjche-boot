package com.boot.admin.system.modules.system.dto;

import lombok.Data;

/**
 * <p>
 * 修改密码入参
 * </p>
 *
 * @author miaoyj
 * @since 2020-12-21
 * @version 1.0.10-SNAPSHOT
 */
@Data
public class UserResetPassDTO {
    private String username;
    private String newPass;
}
