package com.boot.admin.common.pojo;

import lombok.Data;

/**
 * <p>
 * response包装操作接口
 * </p>
 *
 * @author miaoyj
 * @since 2021-04-28
 * @version 1.0.0-SNAPSHOT
 */
@Data
public abstract class AbstractResultWrapper {
    private int code;
    private String message;
}
