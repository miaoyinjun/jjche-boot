package com.boot.admin.system.modules.quartz.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import cn.hutool.log.StaticLog;
import com.alicp.jetcache.Cache;
import com.boot.admin.config.thread.ThreadPoolExecutorUtil;
import com.boot.admin.system.modules.quartz.domain.QuartzJobDO;
import com.boot.admin.system.modules.quartz.domain.QuartzLogDO;
import com.boot.admin.system.modules.quartz.mapper.QuartzLogMapper;
import com.boot.admin.system.modules.quartz.service.QuartzJobService;
import com.boot.admin.tool.modules.tool.service.EmailService;
import com.boot.admin.tool.modules.tool.vo.EmailVO;
import com.boot.admin.cache.service.RedisService;
import com.boot.admin.common.util.ThrowableUtil;
import com.boot.admin.core.util.SpringContextHolder;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 参考人人开源，https://gitee.com/renrenio/renren-security
 *
 * @author /
 * @version 1.0.8-SNAPSHOT
 * @since 2019-01-07
 */
@Async
@SuppressWarnings({"unchecked", "all"})
public class ExecutionJob extends QuartzJobBean {

    /**
     * 该处仅供参考
     */
    private final static ThreadPoolExecutor EXECUTOR = ThreadPoolExecutorUtil.getPoll();

    /** {@inheritDoc} */
    @Override
    public void executeInternal(JobExecutionContext context) {
        QuartzJobDO quartzJob = (QuartzJobDO) context.getMergedJobDataMap().get(QuartzJobDO.JOB_KEY);
        QuartzManage quartzManage = SpringContextHolder.getBean(QuartzManage.class);
        Cache quartzCache = quartzManage.quartzCache;
        Long id = quartzJob.getId();
        String uuid = quartzJob.getUuid();
        //分布式锁，30分钟
        quartzCache.tryLockAndRun(id,
                30, TimeUnit.MINUTES, () -> executionJob(quartzJob, uuid));
    }

    private void executionJob(QuartzJobDO quartzJob, String uuid) {
        // 获取spring bean
        QuartzLogMapper quartzLogMapper = SpringContextHolder.getBean(QuartzLogMapper.class);
        QuartzJobService quartzJobService = SpringContextHolder.getBean(QuartzJobService.class);
        RedisService redisService = SpringContextHolder.getBean(RedisService.class);
        QuartzLogDO log = new QuartzLogDO();
        log.setJobName(quartzJob.getJobName());
        log.setBeanName(quartzJob.getBeanName());
        log.setMethodName(quartzJob.getMethodName());
        log.setParams(quartzJob.getParams());
        long startTime = System.currentTimeMillis();
        log.setCronExpression(quartzJob.getCronExpression());
        try {
            // 执行任务
            StaticLog.warn("--------------------------------------------------------------");
            StaticLog.warn("任务开始执行，任务名称：" + quartzJob.getJobName());
            QuartzRunnable task = new QuartzRunnable(quartzJob.getBeanName(), quartzJob.getMethodName(),
                    quartzJob.getParams());
            Future<?> future = EXECUTOR.submit(task);
            future.get();
            long times = System.currentTimeMillis() - startTime;
            log.setTime(times);
            if (com.boot.admin.common.util.StrUtil.isNotBlank(uuid)) {
                redisService.objectSetObject(uuid, true);
            }
            // 任务状态
            log.setIsSuccess(true);
            StaticLog.warn("任务执行完毕，任务名称：" + quartzJob.getJobName() + ", 执行时间：" + times + "毫秒");
            StaticLog.warn("--------------------------------------------------------------");
            // 判断是否存在子任务
            if (quartzJob.getSubTask() != null) {
                String subTask = quartzJob.getSubTask();
                if (StrUtil.isNotBlank(subTask)) {
                    String[] tasks = subTask.split("[,，]");
                    // 执行子任务
                    quartzJobService.executionSubJob(tasks);
                }
            }
        } catch (Exception e) {
            if (com.boot.admin.common.util.StrUtil.isNotBlank(uuid)) {
                redisService.setAddSetObject(uuid, false);
            }
            StaticLog.warn("任务执行失败，任务名称：" + quartzJob.getJobName());
            StaticLog.warn("--------------------------------------------------------------");
            long times = System.currentTimeMillis() - startTime;
            log.setTime(times);
            // 任务状态 0：成功 1：失败
            log.setIsSuccess(false);
            log.setExceptionDetail(ThrowableUtil.getStackTrace(e));
            // 任务如果失败了则暂停
            if (quartzJob.getPauseAfterFailure() != null && quartzJob.getPauseAfterFailure()) {
                quartzJob.setIsPause(false);
                //更新状态
                quartzJobService.updateIsPause(quartzJob);
            }
            if (StrUtil.isNotBlank(quartzJob.getEmail())) {
                EmailService emailService = SpringContextHolder.getBean(EmailService.class);
                // 邮箱报警
                EmailVO emailVo = taskAlarm(quartzJob, ThrowableUtil.getStackTrace(e));
                emailService.send(emailVo, emailService.find());
            }
        } finally {
            quartzLogMapper.insert(log);
        }
    }


    private EmailVO taskAlarm(QuartzJobDO quartzJob, String msg) {
        EmailVO emailVo = new EmailVO();
        emailVo.setSubject("定时任务【" + quartzJob.getJobName() + "】执行失败，请尽快处理！");
        Map<String, Object> data = new HashMap<>(16);
        data.put("task", quartzJob);
        data.put("msg", msg);
        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("template", TemplateConfig.ResourceMode.CLASSPATH));
        Template template = engine.getTemplate("email/taskAlarm.ftl");
        emailVo.setContent(template.render(data));
        List<String> emails = Arrays.asList(quartzJob.getEmail().split("[,，]"));
        emailVo.setTos(emails);
        return emailVo;
    }
}
