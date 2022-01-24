package org.jjche.demo.modules.student.feign;

import org.jjche.core.wrapper.response.ResultWrapper;
import org.jjche.demo.modules.student.feign.fallback.JeecgHelloFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 * JeecgHelloApi
 * </p>
 *
 * @author miaoyj
 * @since 2022-01-21
 */
@FeignClient(value = "jeecg-cloud-server", fallbackFactory = JeecgHelloFallback.class)
public interface JeecgHelloApi {

    /**
     * 根据service_path获取api配置信息
     *
     * @param name /
     * @return /
     */
    @GetMapping(value = "/api/cloud/server")
    ResultWrapper<String> server(@RequestParam(value = "name") String name);
}
