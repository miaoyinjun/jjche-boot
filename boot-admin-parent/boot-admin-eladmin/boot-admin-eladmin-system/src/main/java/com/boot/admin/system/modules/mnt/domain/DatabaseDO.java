package com.boot.admin.system.modules.mnt.domain;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.annotation.TableName;
import com.boot.admin.mybatis.base.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>DatabaseDO class.</p>
 *
 * @author zhanghouying
 * @since 2019-08-24
 * @version 1.0.8-SNAPSHOT
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(value = "mnt_database")
public class DatabaseDO extends BaseEntity {

    @ApiModelProperty(value = "数据库名称")
    private String name;

    @ApiModelProperty(value = "数据库连接地址")
    private String jdbcUrl;

	@ApiModelProperty(value = "数据库密码")
    private String pwd;

	@ApiModelProperty(value = "用户名")
    private String userName;

    /**
     * <p>copy.</p>
     *
     * @param source a {@link com.boot.admin.system.modules.mnt.domain.DatabaseDO} object.
     */
    public void copy(DatabaseDO source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
