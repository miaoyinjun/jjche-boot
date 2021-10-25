package com.boot.admin.system.modules.system.dto;

import com.boot.admin.common.annotation.QueryCriteria;
import com.boot.admin.common.dto.BaseQueryCriteriaDTO;
import lombok.Data;

import java.util.List;

/**
 * <p>MenuQueryCriteriaDTO class.</p>
 *
 * @author Zheng Jie
 * 公共查询类
 * @version 1.0.8-SNAPSHOT
 */
@Data
public class MenuQueryCriteriaDTO extends BaseQueryCriteriaDTO {

    private String blurry;

    @QueryCriteria(type = QueryCriteria.Type.BETWEEN)
    private List<String> gmtCreate;

    @QueryCriteria(type = QueryCriteria.Type.IS_NULL, propName = "pid")
    private Boolean pidIsNull;

    @QueryCriteria
    private Long pid;

    @QueryCriteria
    private Integer type;
}
