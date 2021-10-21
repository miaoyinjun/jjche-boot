package com.boot.admin.system.modules.system.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.boot.admin.mybatis.base.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * <p>
 * 菜单数据字段权限表
 * </p>
 *
 * @author miaoyj
 * @since 2020-11-17
 * @version 1.0.10-SNAPSHOT
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_data_permission_field")
public class DataPermissionFieldDO extends BaseEntity {

    private Long menuId;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "标识")
    private String code;

    @ApiModelProperty(value = "排序")
    private Integer sort = 999;

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
}
