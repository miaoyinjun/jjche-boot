package com.boot.admin.demo.modules.student.api.dto;

import com.boot.admin.demo.modules.student.api.enums.CourseEnum;
import com.boot.admin.common.dto.BaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 学生
 * </p>
 *
 * @author miaoyj
 * @since 2021-02-02
 * @version 1.0.0-SNAPSHOT
 */
@Data
public class StudentDTO extends BaseDTO {
   @NotNull(message = "id不能为空", groups = Update.class)
   private Long id;
   @NotBlank(message = "姓名不能为空")
   @ApiModelProperty(value = "姓名")
   private String name;
   @NotNull(message = "年龄不能为空")
   @ApiModelProperty(value = "年龄")
   private Integer age;
   @ApiModelProperty(value = "课程类型")
   private CourseEnum course;
   @ApiModelProperty(value = "所属用户id")
   private Long creatorUserId;
}
