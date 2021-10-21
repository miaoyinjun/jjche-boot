package com.boot.admin.core.alarm.dd;

import com.boot.admin.core.constant.CoreConstant;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 * 钉钉
 * </p>
 *
 * @author miaoyj
 * @since 2020-12-16
 * @version 1.0.10-SNAPSHOT
 */
@FeignClient(name = "ALARM-DING-TALK", url = "${" + CoreConstant.PROPERTY_CORE_ALARM_DING_TALK_URL_PACKAGE_PREFIX + "}")
public interface DingTalkFeignClient {
    /**
     * <p>send.</p>
     *
     * @param accessToken a {@link java.lang.String} object.
     * @param dingTalkDTO a {@link com.boot.admin.core.alarm.dd.DingTalkDTO} object.
     * @return a {@link org.springframework.http.ResponseEntity} object.
     */
    @PostMapping
    ResponseEntity send(@RequestParam("access_token") String accessToken, @RequestBody DingTalkDTO dingTalkDTO);

}
