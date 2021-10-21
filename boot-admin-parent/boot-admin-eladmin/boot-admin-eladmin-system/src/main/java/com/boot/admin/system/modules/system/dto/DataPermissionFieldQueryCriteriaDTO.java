package com.boot.admin.system.modules.system.dto;

import com.boot.admin.common.annotation.QueryCriteria;
import lombok.Data;

/**
 * <p>
 * 数据权限列查询
 * </p>
 *
 * @author miaoyj
 * @since 2020-11-18
 * @version 1.0.10-SNAPSHOT
 */
@Data
public class DataPermissionFieldQueryCriteriaDTO {

    @QueryCriteria
    private Long menuId;
}
