package org.jjche.demo.modules.a.api.dto;

import lombok.Data;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.sql.Timestamp;
import org.jjche.common.annotation.QueryCriteria;
import org.jjche.common.dto.BaseDTO;
import org.jjche.common.dto.BaseQueryCriteriaDTO;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

/**
* <p>
* ss
* </p>
*
* @author miaoyj
* @since 2022-07-19
*/
@Data
@EqualsAndHashCode(callSuper = true)
public class TeacherDTO extends BaseQueryCriteriaDTO implements BaseDTO {
   @ApiModelProperty(value = "ID")
   @NotNull(message = "id不能为空", groups = Update.class)
   private Long id;
   @ApiModelProperty(value = "姓名")
   @NotBlank(message = "姓名不能为空")
   @Length(max = 55, message = "姓名最大长度不能超过55")
   private String name;
   @ApiModelProperty(value = "年龄")
   @NotNull(message = "年龄不能为空")
   private Integer age;
   @ApiModelProperty(value = "课程")
   @Length(max = 50, message = "课程最大长度不能超过50")
   private String course;
   @ApiModelProperty(value = "所属用户id")
   private Long creatorUserId;
}