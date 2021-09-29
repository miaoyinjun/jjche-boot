package com.boot.admin.tool.modules.tool.dto;

import com.boot.admin.common.annotation.QueryCriteria;
import com.boot.admin.common.dto.BaseQueryCriteriaDTO;
import lombok.Data;

import java.util.List;

/**
 * <p>QiniuQueryCriteriaDTO class.</p>
 *
 * @author Zheng Jie
 * @since 2019-6-4 09:54:37
 * @version 1.0.8-SNAPSHOT
 */
@Data
public class QiniuQueryCriteriaDTO extends BaseQueryCriteriaDTO {

    @QueryCriteria(type = QueryCriteria.Type.LIKE)
    private String key;

    @QueryCriteria(type = QueryCriteria.Type.BETWEEN)
    private List<String> gmtCreate;
}
