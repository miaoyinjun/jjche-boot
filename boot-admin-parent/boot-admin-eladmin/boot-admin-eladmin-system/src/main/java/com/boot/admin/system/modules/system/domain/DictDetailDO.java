package com.boot.admin.system.modules.system.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.boot.admin.mybatis.base.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>DictDetailDO class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2019-04-10
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_dict_detail")
public class DictDetailDO extends BaseEntity {

    private Long dictId;

    @ApiModelProperty(value = "字典标签")
    private String label;

    @ApiModelProperty(value = "字典值")
    private String value;

    @ApiModelProperty(value = "排序")
    private Integer dictSort = 999;
}
