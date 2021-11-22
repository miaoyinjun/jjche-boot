package com.boot.admin.system.modules.system.api.dto;

import com.boot.admin.common.annotation.QueryCriteria;
import com.boot.admin.common.dto.BaseQueryCriteriaDTO;
import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>UserQueryCriteriaDTO class.</p>
 *
 * @author Zheng Jie
 * @since 2018-11-23
 * @version 1.0.8-SNAPSHOT
 */
@Data
public class UserQueryCriteriaDTO extends BaseQueryCriteriaDTO {

    @QueryCriteria
    private Long id;

    @QueryCriteria(propName = "dept_id", type = QueryCriteria.Type.IN)
    private Set<Long> deptIds = new HashSet<>();

    private String blurry;

    @QueryCriteria
    private Boolean enabled;

    @QueryCriteria(type = QueryCriteria.Type.BETWEEN)
    private List<String> gmtCreate;

    private Long deptId;
}
