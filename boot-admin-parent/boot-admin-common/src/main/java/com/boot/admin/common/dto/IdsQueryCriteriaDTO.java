package com.boot.admin.common.dto;

import com.boot.admin.common.annotation.QueryCriteria;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

/**
 * <p>
 * 查询多个ID
 * </p>
 *
 * @author miaoyj
 * @since 2021-10-22
 * @version 1.0.1-SNAPSHOT
 */
@Data
public class IdsQueryCriteriaDTO extends BaseQueryCriteriaDTO {

    /**
     * 精确
     */
    @ApiModelProperty(value = "id")
    @QueryCriteria(propName = "id", type = QueryCriteria.Type.IN)
    @NotEmpty(message = "ids不能为空")
    private Set<Long> ids;
}
