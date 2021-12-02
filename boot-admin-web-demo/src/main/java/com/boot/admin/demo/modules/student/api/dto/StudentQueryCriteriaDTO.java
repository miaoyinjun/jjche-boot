package com.boot.admin.demo.modules.student.api.dto;

import com.boot.admin.common.annotation.QueryCriteria;
import com.boot.admin.common.dto.BaseQueryCriteriaDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * <p>
 * 学生 查询
 * </p>
 *
 * @author miaoyj
 * @since 2021-02-02
 * @version 1.0.0-SNAPSHOT
 */
@Data
public class StudentQueryCriteriaDTO extends BaseQueryCriteriaDTO {

    /**
     * 精确
     */
    @ApiModelProperty(value = "姓名")
    @QueryCriteria(propName = "name", type = QueryCriteria.Type.LIKE)
    private String name;
}
