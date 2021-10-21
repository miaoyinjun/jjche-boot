package com.boot.admin.log.modules.logging.dto;

import com.boot.admin.common.annotation.QueryCriteria;
import lombok.Data;

import java.util.List;

/**
 * 日志查询类
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2019-6-4 09:23:07
 */
@Data
public class LogQueryCriteriaDTO {

    private String blurry;

    @QueryCriteria
    private String username;

    @QueryCriteria
    private String logType;

    @QueryCriteria
    private Boolean isSuccess;

    @QueryCriteria
    private String module;

    @QueryCriteria
    private String category;

    @QueryCriteria(type = QueryCriteria.Type.BETWEEN)
    private List<String> gmtCreate;
}
