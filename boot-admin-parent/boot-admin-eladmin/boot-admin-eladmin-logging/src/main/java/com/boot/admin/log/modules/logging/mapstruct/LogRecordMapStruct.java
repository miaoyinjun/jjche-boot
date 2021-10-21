package com.boot.admin.log.modules.logging.mapstruct;

import com.boot.admin.log.modules.logging.domain.LogDO;
import com.boot.admin.log.biz.beans.LogRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

/**
 * <p>
 * LogRecord, log映射
 * </p>
 *
 * @author miaoyj
 * @since 2021-04-30
 * @version 1.0.0-SNAPSHOT
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LogRecordMapStruct {

    @Mappings({
            @Mapping(source = "operator", target = "username"),
            @Mapping(source = "value", target = "description"),
            @Mapping(source = "type", target = "logType"),
            @Mapping(source = "success", target = "isSuccess"),
    })
    /**
     * <p>
     * LogRecord转换Log
     * </p>
     *
     * @param logRecord 日志记录
     * @return LogDO
     */
    LogDO toLog(LogRecord logRecord);
}
