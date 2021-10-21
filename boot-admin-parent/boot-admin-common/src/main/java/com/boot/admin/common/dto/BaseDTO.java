package com.boot.admin.common.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

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
@Data
@NoArgsConstructor
public class BaseDTO implements Serializable {
    /* 分组校验 */
    public @interface Create {
    }

    /* 分组校验 */
    public @interface Update {
    }
}
