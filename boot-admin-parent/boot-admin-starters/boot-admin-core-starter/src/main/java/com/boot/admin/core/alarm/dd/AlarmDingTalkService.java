package com.boot.admin.core.alarm.dd;

import cn.hutool.core.util.StrUtil;
import cn.hutool.log.StaticLog;
import com.boot.admin.core.property.CoreAlarmDingTalkProperties;
import com.boot.admin.core.property.CoreAlarmProperties;
import com.boot.admin.core.property.CoreProperties;
import com.boot.admin.core.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 钉钉告警服务
 * </p>
 *
 * @author miaoyj
 * @since 2020-12-16
 * @version 1.0.10-SNAPSHOT
 */
@Service
public class AlarmDingTalkService {

    @Value("${spring.application.name:}")
    private String appName;
    @Autowired
    private CoreProperties coreProperties;
    @Autowired
    private DingTalkFeignClient dingTalkFeignClient;
    /**
     * <p>
     * 发送钉钉告警
     * </p>
     *
     * @param content 内容
     * @return 是否成功
     * @author miaoyj
     * @since 2020-12-16
     */
    public boolean sendAlarm(String content) {
        try {
            CoreAlarmProperties alarm = coreProperties.getAlarm();
            CoreAlarmDingTalkProperties dingTalk = alarm.getDingTalk();
            String accessToken = dingTalk.getAccessToken();
            if(StrUtil.isNotBlank(accessToken)){
                String userName = SecurityUtils.getCurrentOrDefaultUsername();
                String errorMsg = StrUtil.format("【{}】，业务告警，{}，操作人：{}", appName, content, userName);
                DingTalkContentDTO text = new DingTalkContentDTO();
                text.setContent(errorMsg);
                DingTalkDTO dingTalkDTO = new DingTalkDTO("text", text);
                ResponseEntity responseEntity = dingTalkFeignClient.send(accessToken, dingTalkDTO);
            }
        } catch (Exception e) {
            StaticLog.error("sendAlarm error:{}", e);
        }
        return true;
    }
}
