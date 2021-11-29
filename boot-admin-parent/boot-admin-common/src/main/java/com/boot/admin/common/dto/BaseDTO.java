package com.boot.admin.common.dto;

import java.io.Serializable;

/**
 * <p>
 * DTO基类
 * </p>
 *
 * @author miaoyj
 * @since 2021-04-25
 * @version 1.0.0-SNAPSHOT
 */
public interface BaseDTO extends Serializable {

    /* 分组校验 */
    public @interface Create {
    }

    /* 分组校验 */
    public @interface Update {
    }
}
