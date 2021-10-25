package com.boot.admin.system.modules.quartz.dto;

import com.boot.admin.common.annotation.QueryCriteria;
import com.boot.admin.common.dto.BaseQueryCriteriaDTO;
import lombok.Data;

import java.util.List;

/**
 * <p>JobQueryCriteriaDTO class.</p>
 *
 * @author Zheng Jie
 * @since 2019-6-4 10:33:02
 * @version 1.0.8-SNAPSHOT
 */
@Data
public class JobQueryCriteriaDTO extends BaseQueryCriteriaDTO {

    @QueryCriteria(type = QueryCriteria.Type.INNER_LIKE)
    private String jobName;

    @QueryCriteria
    private Boolean isSuccess;

    @QueryCriteria(type = QueryCriteria.Type.BETWEEN)
    private List<String> gmtCreate;
}
