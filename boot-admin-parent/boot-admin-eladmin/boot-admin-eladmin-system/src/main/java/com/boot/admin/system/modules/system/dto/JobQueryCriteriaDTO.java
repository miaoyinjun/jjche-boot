package com.boot.admin.system.modules.system.dto;

import com.boot.admin.common.annotation.QueryCriteria;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * <p>JobQueryCriteriaDTO class.</p>
 *
 * @author Zheng Jie
 * @since 2019-6-4 14:49:34
 * @version 1.0.8-SNAPSHOT
 */
@Data
@NoArgsConstructor
public class JobQueryCriteriaDTO {

    @QueryCriteria(type = QueryCriteria.Type.INNER_LIKE)
    private String name;

    @QueryCriteria
    private Boolean enabled;

    @QueryCriteria(type = QueryCriteria.Type.BETWEEN)
    private List<String> gmtCreate;
}
