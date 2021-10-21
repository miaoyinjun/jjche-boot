package com.boot.admin.system.modules.version.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.boot.admin.mybatis.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 版本
 * </p>
 *
 * @author miaoyj
 * @since 2021-04-23
 * @version 1.0.0-SNAPSHOT
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("sys_version")
public class VersionDO extends BaseEntity {

    /**
    * 版本号名称
    */
    private String name;
    /**
     * 备注
     */
    private String remark;
    /**
    * 是否激活：0:未激活, 1:已激活
    */
    private Boolean isActivated;
}
