package com.boot.admin.system.modules.mnt.dto;

import com.boot.admin.common.annotation.QueryCriteria;
import lombok.Data;

import java.util.List;

/**
 * <p>AppQueryCriteriaDTO class.</p>
 *
 * @author zhanghouying
 * @since 2019-08-24
 * @version 1.0.8-SNAPSHOT
 */
@Data
public class AppQueryCriteriaDTO {

	/**
	 * 模糊
	 */
    @QueryCriteria(type = QueryCriteria.Type.INNER_LIKE)
    private String name;

    @QueryCriteria(type = QueryCriteria.Type.BETWEEN)
    private List<String> gmtCreate;
}
