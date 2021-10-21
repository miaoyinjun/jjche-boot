package com.boot.admin.system.modules.system.dto;

import com.boot.admin.common.annotation.QueryCriteria;
import lombok.Data;

import java.util.List;

/**
 * <p>RoleQueryCriteriaDTO class.</p>
 *
 * @author Zheng Jie
 * 公共查询类
 * @version 1.0.8-SNAPSHOT
 */
@Data
public class RoleQueryCriteriaDTO {

    private String blurry;

    @QueryCriteria(type = QueryCriteria.Type.BETWEEN)
    private List<String> gmtCreate;
}
