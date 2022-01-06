package org.jjche.tool.modules.tool.config;


import org.jjche.common.yml.CoreEnvironmentPostProcessor;

/**
 * <p>
 * 自定义加载yml类
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-07-09
 */
public class ToolEnvironmentPostProcessor extends CoreEnvironmentPostProcessor {
    /**
     * <p>Constructor for SwaggerEnvironmentPostProcessor</p>
     */
    public ToolEnvironmentPostProcessor() {
        super.setYmlName("tool.yml");
    }
}
