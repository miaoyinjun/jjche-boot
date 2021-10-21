package com.boot.admin.system.modules.version.dto;

import com.boot.admin.common.annotation.QueryCriteria;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 版本 查询
 * </p>
 *
 * @author miaoyj
 * @since 2021-04-23
 * @version 1.0.0-SNAPSHOT
 */
@Data
public class VersionQueryCriteriaDTO{


    /**
    * 模糊
    */
    @ApiModelProperty(value = "版本号名称", required = false)
    @QueryCriteria(propName = "name", type = QueryCriteria.Type.INNER_LIKE)
    private String name;
}
