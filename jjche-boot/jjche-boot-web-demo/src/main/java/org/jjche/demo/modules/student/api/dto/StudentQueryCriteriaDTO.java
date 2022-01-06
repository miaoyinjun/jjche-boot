package org.jjche.demo.modules.student.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jjche.common.annotation.QueryCriteria;
import org.jjche.common.dto.BaseQueryCriteriaDTO;

/**
 * <p>
 * 学生 查询
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-02-02
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
