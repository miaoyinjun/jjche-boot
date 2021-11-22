package com.boot.admin.demo.modules.student.api.vo;

import com.boot.admin.demo.modules.student.api.enums.CourseEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
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
public class StudentVO implements Serializable {
   @ApiModelProperty(value = "")
   private Long id;
   @ApiModelProperty(value = "姓名")
   private String name;
   @ApiModelProperty(value = "年龄")
   private Integer age;
   @ApiModelProperty(value = "课程类型")
   private CourseEnum course;
   @ApiModelProperty(value = "创建时间", dataType = "java.lang.String")
   private Timestamp gmtCreate;
}
