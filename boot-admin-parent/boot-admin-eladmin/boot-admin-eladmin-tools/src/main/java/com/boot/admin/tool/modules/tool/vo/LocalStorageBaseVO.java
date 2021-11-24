package com.boot.admin.tool.modules.tool.vo;

import com.boot.admin.common.annotation.EncryptField;
import com.boot.admin.common.enums.FileType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>LocalStorageDO class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2019-09-05
 */
@Data
public class LocalStorageBaseVO implements Serializable {

    @EncryptField
    private String id;

    @ApiModelProperty(value = "后缀")
    private String suffix;

    @ApiModelProperty(value = "路径")
    private String path;

    @ApiModelProperty(value = "类型")
    private FileType type;

    @ApiModelProperty(value = "大小")
    private String size;
}
