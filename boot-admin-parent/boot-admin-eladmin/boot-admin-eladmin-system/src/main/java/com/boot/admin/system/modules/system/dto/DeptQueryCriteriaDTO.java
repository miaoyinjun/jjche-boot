package com.boot.admin.system.modules.system.dto;

import com.boot.admin.common.annotation.QueryCriteria;
import lombok.Data;

import java.util.List;

/**
 * <p>DeptQueryCriteriaDTO class.</p>
 *
 * @author Zheng Jie
 * @since 2019-03-25
 * @version 1.0.8-SNAPSHOT
 */
@Data
public class DeptQueryCriteriaDTO {

    @QueryCriteria(type = QueryCriteria.Type.INNER_LIKE)
    private String name;

    @QueryCriteria
    private Boolean enabled;

    @QueryCriteria
    private Long pid;

    @QueryCriteria(type = QueryCriteria.Type.IS_NULL, propName = "pid")
    private Boolean pidIsNull;

    @QueryCriteria(type = QueryCriteria.Type.BETWEEN)
    private List<String> gmtCreate;
}
