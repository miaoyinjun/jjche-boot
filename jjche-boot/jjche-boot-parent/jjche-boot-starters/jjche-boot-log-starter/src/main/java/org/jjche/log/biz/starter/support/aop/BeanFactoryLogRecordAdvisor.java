package org.jjche.log.biz.starter.support.aop;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;

/**
 * <p>
 * 日志配置
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-04-30
 */
public class BeanFactoryLogRecordAdvisor extends AbstractBeanFactoryPointcutAdvisor {

    private final LogRecordPointcut pointcut = new LogRecordPointcut();
    private LogRecordOperationSource logRecordOperationSource;

    /**
     * <p>setClassFilter.</p>
     *
     * @param classFilter a {@link org.springframework.aop.ClassFilter} object.
     */
    public void setClassFilter(ClassFilter classFilter) {
        this.pointcut.setClassFilter(classFilter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Pointcut getPointcut() {
        return pointcut;
    }

    /**
     * <p>Setter for the field <code>logRecordOperationSource</code>.</p>
     *
     * @param logRecordOperationSource a {@link LogRecordOperationSource} object.
     */
    public void setLogRecordOperationSource(LogRecordOperationSource logRecordOperationSource) {
        pointcut.setLogRecordOperationSource(logRecordOperationSource);
    }
}
