package org.jjche.log.biz.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.log.StaticLog;
import org.jjche.log.biz.beans.LogRecord;
import org.jjche.log.biz.service.ILogRecordService;

import java.util.List;

/**
 * <p>
 * 缺省日志记录回调
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-04-30
 */
public class DefaultLogRecordServiceImpl implements ILogRecordService {

    /**
     * {@inheritDoc}
     */
    @Override
    public void record(LogRecord logRecord) {
        StaticLog.info("【logRecord】log={}", logRecord);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LogRecord> queryLog(String bizKey) {
        return CollUtil.newArrayList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<LogRecord> queryLogByBizNo(String bizNo) {
        return CollUtil.newArrayList();
    }
}
