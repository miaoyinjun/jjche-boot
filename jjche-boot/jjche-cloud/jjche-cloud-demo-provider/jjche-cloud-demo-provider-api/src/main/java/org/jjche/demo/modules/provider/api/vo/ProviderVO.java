package org.jjche.demo.modules.provider.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jjche.demo.modules.provider.api.enums.ProviderCourseEnum;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <p>
 * 学生
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-02-02
 */
@Data
public class ProviderVO implements Serializable {
    @ApiModelProperty(value = "")
    private Long id;
    @ApiModelProperty(value = "姓名")
    private String name;
    @ApiModelProperty(value = "年龄")
    private Integer age;
    @ApiModelProperty(value = "课程类型")
    private ProviderCourseEnum course;
    @ApiModelProperty(value = "创建时间", dataType = "java.lang.String")
    private Timestamp gmtCreate;
    @ApiModelProperty(value = "创建者")
    private String createdBy;
}
