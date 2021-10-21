package com.boot.admin.sba;

import com.boot.admin.sba.actuator.health.DbCountAutoConfiguration;
import com.boot.admin.sba.conf.SbaSecurityConfig;
import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * <p>
 * 入口
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-07-09
 */
@Import({SbaSecurityConfig.class, DbCountAutoConfiguration.class})
@Configuration
@EnableAdminServer
public class SbaAutoConfiguration {
}
