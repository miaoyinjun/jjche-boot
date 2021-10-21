package com.boot.admin.system.modules.system.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 数据权限列输出
 * </p>
 *
 * @author miaoyj
 * @version 1.0.8-SNAPSHOT
 * @since 2020-10-19
 */
@Data
public class DataPermissionFieldDTO implements Serializable {
    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "菜单id")
    @NotNull
    private Long menuId;
    @ApiModelProperty(value = "名称")
    @NotBlank
    private String name;
    @ApiModelProperty(value = "标识")
    @NotBlank
    private String code;
    @ApiModelProperty(value = "排序")
    @NotNull
    private Integer sort;

    /* 分组校验 */
    public @interface Create {}

    /* 分组校验 */
    public @interface Update {}
}
