package com.boot.admin.tool.modules.tool.dto;

import com.boot.admin.common.enums.FileType;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <p>LocalStorageDTO class.</p>
 *
 * @author Zheng Jie
 * @since 2019-09-05
 * @version 1.0.8-SNAPSHOT
 */
@Data
public class LocalStorageDTO implements Serializable {

    private String id;

    private String realName;

    private String name;

    private String suffix;

    private FileType type;

    private String size;

    private Timestamp gmtCreate;

    private String createdBy;
}
