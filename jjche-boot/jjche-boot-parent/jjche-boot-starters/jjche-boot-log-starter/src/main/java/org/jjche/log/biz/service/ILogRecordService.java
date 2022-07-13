package org.jjche.log.biz.service;


import org.jjche.common.dto.LogRecordDTO;

import java.util.List;
import java.util.logging.LogRecord;

/**
 * <p>
 * 日志记录回调接口
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-04-30
 */
public interface ILogRecordService {
    /**
     * 保存log
     *
     * @param logRecord 日志实体
     */
    void record(LogRecordDTO logRecord);

    /**
     * <p>批量保存log</p>
     * @param records 日志实体列表
     * */
    void batchRecord(List<LogRecord> records);

    /**
     * 返回最多100条记录
     *
     * @param bizKey 日志前缀+bizNo
     * @return 操作日志列表
     */
    List<LogRecordDTO> queryLog(String bizKey);

    /**
     * 返回最多100条记录
     *
     * @param bizNo 业务标识
     * @return 操作日志列表
     */
    List<LogRecordDTO> queryLogByBizNo(String bizNo);
}
