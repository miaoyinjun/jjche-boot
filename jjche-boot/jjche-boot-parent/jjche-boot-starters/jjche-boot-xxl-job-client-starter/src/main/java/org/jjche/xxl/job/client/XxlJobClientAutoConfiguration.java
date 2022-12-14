package org.jjche.xxl.job.client;

import cn.hutool.log.StaticLog;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import org.jjche.xxl.job.client.config.XxlJobConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * <p>
 * 入口
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-08-10
 */
@Configuration
@Import(XxlJobConfig.class)
public class XxlJobClientAutoConfiguration {
    @Autowired
    private XxlJobConfig xxlJobConfig;

    /**
     * <p>xxlJobExecutor.</p>
     *
     * @return a {@link com.xxl.job.core.executor.impl.XxlJobSpringExecutor} object.
     */
    @Bean
    public XxlJobSpringExecutor xxlJobExecutor() {
        StaticLog.info(">>>>>>>>>>> xxl-job config init.");
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(xxlJobConfig.getAdminAddresses());
        xxlJobSpringExecutor.setAppname(xxlJobConfig.getAppName());
        xxlJobSpringExecutor.setAddress(xxlJobConfig.getAddress());
        xxlJobSpringExecutor.setIp(xxlJobConfig.getIp());
        xxlJobSpringExecutor.setPort(xxlJobConfig.getPort());
        xxlJobSpringExecutor.setAccessToken(xxlJobConfig.getAccessToken());
        xxlJobSpringExecutor.setLogPath(xxlJobConfig.getLogPath());
        xxlJobSpringExecutor.setLogRetentionDays(xxlJobConfig.getLogRetentionDays());

        return xxlJobSpringExecutor;
    }
}
