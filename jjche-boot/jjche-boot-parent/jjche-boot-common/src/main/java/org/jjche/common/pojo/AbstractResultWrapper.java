package org.jjche.common.pojo;

import lombok.Data;

/**
 * <p>
 * response包装操作接口
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-04-28
 */
@Data
public abstract class AbstractResultWrapper {
    private int code;
    private String message;
}
