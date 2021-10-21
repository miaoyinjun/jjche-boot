package com.boot.admin.system.modules.system.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.boot.admin.mybatis.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * <p>
 * 菜单数据字段权限角色表
 * </p>
 *
 * @author miaoyj
 * @version 1.0.10-SNAPSHOT
 * @since 2020-11-17
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_data_permission_field_role")
public class DataPermissionFieldRoleDO extends BaseEntity {

    private Long menuId;

    private Long roleId;

    private Long dataPermissionFieldId;

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
}
