package com.boot.admin.mybatis.env;

import com.boot.admin.common.yml.CoreEnvironmentPostProcessor;

/**
 * <p>
 * 自定义加载yml类
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-07-09
 */
public class MybatisEnvironmentPostProcessor extends CoreEnvironmentPostProcessor {
    /**
     * <p>Constructor for MybatisEnvironmentPostProcessor</p>
     */
    public MybatisEnvironmentPostProcessor() {
        super.setYmlName("mybatis.yml");
        super.setExtYmlName("mybatis-sql.yml", "boot.admin.mybatis.is-print-sql");
    }
}
