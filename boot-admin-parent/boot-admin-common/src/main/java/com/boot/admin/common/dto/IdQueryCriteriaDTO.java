package com.boot.admin.common.dto;

import com.boot.admin.common.annotation.QueryCriteria;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 查询ID
 * </p>
 *
 * @author miaoyj
 * @since 2021-11-10
 * @version 1.0.1-SNAPSHOT
 */
@Data
public class IdQueryCriteriaDTO extends BaseQueryCriteriaDTO {

    /**
     * 精确
     */
    @ApiModelProperty(value = "id")
    @QueryCriteria(propName = "id", type = QueryCriteria.Type.EQUAL)
    @NotNull(message = "id不能为空")
    private Long id;
}
