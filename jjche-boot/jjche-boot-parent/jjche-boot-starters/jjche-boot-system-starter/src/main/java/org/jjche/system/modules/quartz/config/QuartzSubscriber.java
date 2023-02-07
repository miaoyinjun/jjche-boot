package org.jjche.system.modules.quartz.config;

import cn.hutool.json.JSONUtil;
import cn.hutool.log.StaticLog;
import lombok.AllArgsConstructor;
import org.jjche.cache.conf.RedisSubscriber;
import org.jjche.common.enums.RedisTopicEnum;
import org.jjche.system.modules.quartz.domain.QuartzJobDO;
import org.jjche.system.modules.quartz.dto.QuartzRedisMessageDTO;
import org.jjche.system.modules.quartz.enums.QuartzActionEnum;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 定时器的的订阅者
 * </p>
 *
 * @author miaoyj
 * @since 2023-02-07
 */
@Component
@AllArgsConstructor
public class QuartzSubscriber implements RedisSubscriber {

    @Override
    public String getTopic() {
        return RedisTopicEnum.TOPIC_QUARTZ.getTopic();
    }


    @Override
    public void onMessage(Message message, byte[] pattern) {
        String msg = message.getBody().toString();
        StaticLog.info(">> 定时器接收消息：{}", msg);
        QuartzRedisMessageDTO dto = JSONUtil.toBean(msg, QuartzRedisMessageDTO.class);
        QuartzActionEnum actionl = dto.getAction();
        QuartzJobDO quartzJob = dto.getQuartzJob();
    }

}
