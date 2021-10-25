package com.boot.admin.system.modules.system.dto;

import com.boot.admin.common.dto.BaseQueryCriteriaDTO;
import lombok.Data;

/**
 * <p>DictQueryCriteriaDTO class.</p>
 *
 * @author Zheng Jie
 * 公共查询类
 * @version 1.0.8-SNAPSHOT
 */
@Data
public class DictQueryCriteriaDTO extends BaseQueryCriteriaDTO {

    private String blurry;
}
