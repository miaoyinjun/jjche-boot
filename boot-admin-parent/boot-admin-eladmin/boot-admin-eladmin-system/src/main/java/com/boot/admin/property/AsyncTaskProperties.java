package com.boot.admin.property;

import lombok.Data;

/**
 * <p>
 * 线程池配置属性类
 * </p>
 *
 * @author miaoyj
 * @since 2021-01-05
 * @version 1.0.10-SNAPSHOT
 */
@Data
public class AsyncTaskProperties {

    private AsyncTaskPoolProperties pool;
}
