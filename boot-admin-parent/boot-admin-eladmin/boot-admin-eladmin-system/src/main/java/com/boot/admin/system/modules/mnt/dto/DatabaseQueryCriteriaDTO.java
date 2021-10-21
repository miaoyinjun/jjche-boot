package com.boot.admin.system.modules.mnt.dto;

import com.boot.admin.common.annotation.QueryCriteria;
import lombok.Data;

import java.util.List;

/**
 * <p>DatabaseQueryCriteriaDTO class.</p>
 *
 * @author zhanghouying
 * @since 2019-08-24
 * @version 1.0.8-SNAPSHOT
 */
@Data
public class DatabaseQueryCriteriaDTO {

	/**
	 * 模糊
	 */
    @QueryCriteria(type = QueryCriteria.Type.INNER_LIKE)
    private String name;

	/**
	 * 精确
	 */
    @QueryCriteria
    private String jdbcUrl;

    @QueryCriteria(type = QueryCriteria.Type.BETWEEN)
    private List<String> gmtCreate;
}
