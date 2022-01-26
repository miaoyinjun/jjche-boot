package org.jjche;

import org.jjche.common.constant.PackageConstant;
import org.jjche.jackson.JacksonAutoConfiguration;
import org.jjche.log.biz.starter.annotation.EnableLogRecord;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

/**
 * <p>
 * 入口
 * </p>
 *
 * @author miaoyj
 * @since 2022-01-05
 */
@ComponentScan(excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {JacksonAutoConfiguration.class})})
@MapperScan(PackageConstant.MAPPER_PATH_STAR)
@EnableLogRecord(tenant = PackageConstant.BASE_PATH)
@EnableFeignClients
public class JjcheAutoConfiguration {
}
