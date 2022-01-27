package org.jjche;

import org.jjche.common.constant.PackageConstant;
import org.jjche.log.biz.starter.annotation.EnableLogRecord;
import org.mybatis.spring.annotation.MapperScan;

/**
 * <p>
 * 入口
 * </p>
 *
 * @author miaoyj
 * @since 2022-01-05
 */
//@ComponentScan(excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {SecurityBasicAuthFilter.class})})
@MapperScan(PackageConstant.MAPPER_PATH_STAR)
@EnableLogRecord(tenant = PackageConstant.BASE_PATH)
public class AdminAutoConfiguration {
}
