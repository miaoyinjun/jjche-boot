package com.boot.admin.config;


import com.boot.admin.core.util.SecurityUtils;
import com.boot.admin.log.biz.beans.Operator;
import com.boot.admin.log.biz.service.IOperatorGetService;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 日志当前用户
 * </p>
 *
 * @author miaoyj
 * @since 2021-04-29
 * @version 1.0.0-SNAPSHOT
 */
@Component
public class DefaultOperatorGetServiceImpl implements IOperatorGetService {

    /** {@inheritDoc} */
    @Override
    public Operator getUser() {
        Operator operator = new Operator();
        operator.setOperatorId(SecurityUtils.getCurrentOrDefaultUsername());
        return operator;
    }
}
