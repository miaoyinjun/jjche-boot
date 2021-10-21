package com.boot.admin.log.biz.service.impl;


import com.boot.admin.log.biz.beans.Operator;
import com.boot.admin.log.biz.service.IOperatorGetService;

/**
 * <p>
 * 缺省操作人
 * </p>
 *
 * @author miaoyj
 * @since 2021-04-30
 * @version 1.0.0-SNAPSHOT
 */
public class DefaultOperatorGetServiceImpl implements IOperatorGetService {

    /** {@inheritDoc} */
    @Override
    public Operator getUser() {
        Operator operator = new Operator();
        operator.setOperatorId("默认用户");
        return operator;
    }
}
