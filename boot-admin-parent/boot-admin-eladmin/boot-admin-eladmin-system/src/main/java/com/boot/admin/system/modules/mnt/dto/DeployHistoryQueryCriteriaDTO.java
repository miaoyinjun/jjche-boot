package com.boot.admin.system.modules.mnt.dto;

import com.boot.admin.common.annotation.QueryCriteria;
import lombok.Data;

import java.util.List;

/**
 * <p>DeployHistoryQueryCriteriaDTO class.</p>
 *
 * @author zhanghouying
 * @version 1.0.8-SNAPSHOT
 * @since 2019-08-24
 */
@Data
public class DeployHistoryQueryCriteriaDTO {

    /**
     * 精确
     */
    private String blurry;

    @QueryCriteria
    private Long deployId;

    @QueryCriteria(type = QueryCriteria.Type.BETWEEN)
    private List<String> gmtCreate;
}
