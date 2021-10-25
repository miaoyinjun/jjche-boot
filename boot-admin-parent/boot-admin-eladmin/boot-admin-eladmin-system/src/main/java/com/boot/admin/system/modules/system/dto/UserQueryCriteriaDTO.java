package com.boot.admin.system.modules.system.dto;

import com.boot.admin.common.annotation.PermissionData;
import com.boot.admin.common.annotation.QueryCriteria;
import com.boot.admin.common.dto.BaseQueryCriteriaDTO;
import com.boot.admin.common.pojo.DataScope;
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
@PermissionData(deptIdInFieldName = DataScope.F_SQL_SCOPE_NAME)
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
