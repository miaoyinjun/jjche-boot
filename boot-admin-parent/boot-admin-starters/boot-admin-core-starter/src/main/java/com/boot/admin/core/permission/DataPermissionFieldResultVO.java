package com.boot.admin.core.permission;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>DataPermissionFieldResultVO class.</p>
 *
 * @author nikohuang
 * @version 1.0.10-SNAPSHOT
 */
@Data
public class DataPermissionFieldResultVO {

    @JsonIgnore
    Long id;
    /**
     * 排序
     */
    @ApiModelProperty("排序")
    private Integer sort;

    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String name;

    /**
     * 标志符号
     */
    @ApiModelProperty("标志符号")
    private String code;

    /**
     * 是否允许访问
     */
    @ApiModelProperty("是否允许访问")
    private Boolean isAccessible;
}
