package com.boot.admin.system.modules.mnt.dto;

import com.boot.admin.common.annotation.QueryCriteria;
import lombok.Data;

import java.util.List;

/**
 * <p>ServerDeployQueryCriteriaDTO class.</p>
 *
 * @author zhanghouying
 * @since 2019-08-24
 * @version 1.0.8-SNAPSHOT
 */
@Data
public class ServerDeployQueryCriteriaDTO {

	/**
	 * 模糊
	 */
    private String blurry;

    @QueryCriteria(type = QueryCriteria.Type.BETWEEN)
    private List<String> gmtCreate;
}
