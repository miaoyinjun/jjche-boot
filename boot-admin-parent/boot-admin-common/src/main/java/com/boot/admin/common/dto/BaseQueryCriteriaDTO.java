package com.boot.admin.common.dto;

import com.boot.admin.common.annotation.IgnoreSwaggerParameter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 查询基类
 * </p>
 *
 * @author miaoyj
 * @since 2021-10-22
 * @version 1.0.1-SNAPSHOT
 */
@Data
public class BaseQueryCriteriaDTO implements Serializable {

    @IgnoreSwaggerParameter
    @JsonIgnore
    private List<PermissionDataRuleDTO> permissionDataRuleList;
}
