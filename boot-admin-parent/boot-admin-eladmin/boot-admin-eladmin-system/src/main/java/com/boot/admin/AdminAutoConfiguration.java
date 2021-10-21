package com.boot.admin;

import com.boot.admin.common.constant.PackageConstant;
import com.boot.admin.jackson.HttpConvertAutoConfiguration;
import com.boot.admin.log.biz.starter.annotation.EnableLogRecord;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

/**
 * 开启审计功能 @EnableJpaAuditing
 *
 * @author Zheng Jie
 * @since 2018/11/15 9:20:19
 * @version 1.0.8-SNAPSHOT
 */
@ComponentScan(excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,classes = {HttpConvertAutoConfiguration.class})})
@MapperScan(PackageConstant.MAPPER_PATH_STAR)
@EnableLogRecord(tenant = "com.boot.admin")
public class AdminAutoConfiguration {
}
