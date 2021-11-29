package com.boot.admin.system.modules.system.api.dto;

import com.boot.admin.common.annotation.QueryCriteria;
import com.boot.admin.common.dto.BaseQueryCriteriaDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
/**
 * <p>
 * 数据规则 查询
 * </p>
 *
 * @author miaoyj
 * @since 2021-10-27
 * @version 1.0.1-SNAPSHOT
 */
@Data
public class DataPermissionRuleQueryCriteriaDTO extends BaseQueryCriteriaDTO {


    /**
    * 精确
    */
    @NotNull(message = "菜单ID不能为空")
    @ApiModelProperty(value = "菜单ID", required = true)
    @QueryCriteria(propName = "menu_id")
    private Long menuId;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    @QueryCriteria(type = QueryCriteria.Type.LIKE)
    private String name;
}
