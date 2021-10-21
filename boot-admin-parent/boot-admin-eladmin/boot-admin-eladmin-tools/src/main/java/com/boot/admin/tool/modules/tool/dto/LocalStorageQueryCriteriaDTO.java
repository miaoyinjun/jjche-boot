package com.boot.admin.tool.modules.tool.dto;

import com.boot.admin.common.annotation.QueryCriteria;
import lombok.Data;

import java.util.List;

/**
 * <p>LocalStorageQueryCriteriaDTO class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2019-09-05
 */
@Data
public class LocalStorageQueryCriteriaDTO {

    private String blurry;

    @QueryCriteria(type = QueryCriteria.Type.BETWEEN)
    private List<String> gmtCreate;
}
