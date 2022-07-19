package org.jjche.demo.modules.a.api.dto;

import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import io.swagger.annotations.ApiModelProperty;
import java.sql.Timestamp;
import java.math.BigDecimal;
import java.util.List;
import org.jjche.common.annotation.QueryCriteria;
import org.jjche.common.dto.BaseQueryCriteriaDTO;

/**
* <p>
* ss 查询
* </p>
*
* @author miaoyj
* @since 2022-07-19
*/
@Data
public class TeacherQueryCriteriaDTO extends BaseQueryCriteriaDTO{

}