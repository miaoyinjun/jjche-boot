package org.jjche.demo.modules.student.feign;

import org.jjche.core.wrapper.response.ResultWrapper;
import org.jjche.demo.modules.student.feign.fallback.JjcheSysFallback;
import org.jjche.security.dto.UserVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 * JjcheSysApi
 * </p>
 *
 * @author miaoyj
 * @since 2022-01-21
 */
@FeignClient(value = "jjche-cloud-server", fallbackFactory = JjcheSysFallback.class)
public interface JjcheSysApi {

    /**
     * 根据service_path获取api配置信息
     *
     * @param name /
     * @return /
     */
    @GetMapping(value = "/api/cloud/server")
    ResultWrapper<UserVO> server(@RequestParam(value = "name") String name);
}
