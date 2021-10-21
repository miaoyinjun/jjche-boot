package com.boot.admin.system.modules.mnt.dto;

import com.boot.admin.common.annotation.QueryCriteria;
import lombok.Data;

import java.util.List;

/**
 * <p>DeployQueryCriteriaDTO class.</p>
 *
 * @author zhanghouying
 * @version 1.0.8-SNAPSHOT
 * @since 2019-08-24
 */
@Data
public class DeployQueryCriteriaDTO {

    /**
     * 模糊
     */
    @QueryCriteria(type = QueryCriteria.Type.INNER_LIKE, propName = "name")
    private String appName;

    @QueryCriteria(type = QueryCriteria.Type.BETWEEN)
    private List<String> gmtCreate;

}
