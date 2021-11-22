package com.boot.admin.system.modules.system.api.dto;

import com.boot.admin.common.dto.BaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
* <p>
* 数据规则权限
* </p>
*
* @author miaoyj
* @since 2021-11-01
*/
@Data
public class DataPermissionFiledRoleSelectedDTO implements BaseDTO {
   @ApiModelProperty(value = "ID")
   private Long id;
   @ApiModelProperty(value = "可见")
   private Boolean isAccessible;
   @ApiModelProperty(value = "编辑")
   private Boolean isEditable;
}