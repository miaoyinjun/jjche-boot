package com.boot.admin.system.modules.system.vo;

import lombok.Data;

/**
 * 修改密码的 Vo 类
 *
 * @author Zheng Jie
 * @since 2019年7月11日13:59:49
 * @version 1.0.8-SNAPSHOT
 */
@Data
public class UserPassVO {

    private String oldPass;

    private String newPass;
}
