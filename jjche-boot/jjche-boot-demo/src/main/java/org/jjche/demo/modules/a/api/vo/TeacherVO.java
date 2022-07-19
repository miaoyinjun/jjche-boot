package org.jjche.demo.modules.a.api.vo;

import lombok.Data;
import java.io.Serializable;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.sql.Timestamp;
/**
* <p>
* ss
* </p>
*
* @author miaoyj
* @since 2022-07-19
*/
@Data
public class TeacherVO implements Serializable {
   @ApiModelProperty(value = "ID")
   private Long id;
   @ApiModelProperty(value = "姓名")
   private String name;
   @ApiModelProperty(value = "年龄")
   private Integer age;
   @ApiModelProperty(value = "课程")
   private String course;
   @ApiModelProperty(value = "所属用户id")
   private Long creatorUserId;
}