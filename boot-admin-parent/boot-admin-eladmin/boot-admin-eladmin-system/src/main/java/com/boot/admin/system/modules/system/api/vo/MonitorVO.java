package com.boot.admin.system.modules.system.api.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * <p>
 * 监控出参
 * </p>
 *
 * @author miaoyj
 * @since 2020-09-21
 * @version 1.0.8-SNAPSHOT
 */
@Data
public class MonitorVO implements Serializable {
    private Map<String, Object> sys;
    private Map<String, Object> cpu;
    private Map<String, Object> memory;
    private Map<String, Object> swap;
    private Map<String, Object> disk;
    private String time;
}
