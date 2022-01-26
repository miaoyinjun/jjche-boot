package org.jjche;

import org.jjche.common.constant.PackageConstant;
import org.jjche.log.biz.starter.annotation.EnableLogRecord;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * <p>
 * 入口
 * </p>
 *
 * @author miaoyj
 * @since 2022-01-05
 */
@ComponentScan
@MapperScan(PackageConstant.MAPPER_PATH_STAR)
@EnableLogRecord(tenant = PackageConstant.BASE_PATH)
@EnableFeignClients
public class AdminAutoConfiguration {
}
