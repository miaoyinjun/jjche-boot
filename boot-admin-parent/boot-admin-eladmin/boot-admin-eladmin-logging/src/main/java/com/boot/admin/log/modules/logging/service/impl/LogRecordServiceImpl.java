package com.boot.admin.log.modules.logging.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.boot.admin.log.modules.logging.domain.LogDO;
import com.boot.admin.log.modules.logging.mapstruct.LogRecordMapStruct;
import com.boot.admin.log.modules.logging.service.LogService;
import com.boot.admin.log.biz.beans.LogRecord;
import com.boot.admin.log.biz.service.ILogRecordService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>LogRecordServiceImpl class.</p>
 *
 * @author muzhantong
 * create on 2020/4/29 4:34 下午
 * @version 1.0.0-SNAPSHOT
 */
@Component
public class LogRecordServiceImpl implements ILogRecordService {

    @Resource
    private LogService logService;
    @Resource
    private LogRecordMapStruct logRecordMapper;

    /** {@inheritDoc} */
    @Override
    public void record(LogRecord logRecord) {
        LogDO log = logRecordMapper.toLog(logRecord);
        logService.saveLog(log);
    }

    /** {@inheritDoc} */
    @Override
    public List<LogRecord> queryLog(String bizKey) {
        return CollUtil.newArrayList();
    }

    /** {@inheritDoc} */
    @Override
    public List<LogRecord> queryLogByBizNo(String bizNo) {
        return CollUtil.newArrayList();
    }
}
