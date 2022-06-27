package org.jjche.system.modules.quartz.task;

import cn.hutool.core.util.NumberUtil;
import org.jjche.log.modules.logging.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 日志任务
 * </p>
 *
 * @author miaoyj
 * @since 2021-12-13
 */
@Component
public class LogTask {

    @Autowired
    private LogService logService;

    /**
     * <p>
     * 清空N个月之前的操作日志
     * </p>
     *
     * @param month 月
     */
    public void cleanLogs(String month) {
        logService.delMonth(NumberUtil.parseInt(month));
    }

}
