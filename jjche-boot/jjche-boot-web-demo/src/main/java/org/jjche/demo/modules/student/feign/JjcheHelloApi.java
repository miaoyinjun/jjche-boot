package org.jjche.demo.modules.student.feign;

import org.jjche.core.wrapper.response.ResultWrapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 * JjcheHelloApi
 * </p>
 *
 * @author miaoyj
 * @since 2022-01-21
 */
//@FeignClient(value = "jjche-cloud-server", fallbackFactory = JjcheHelloFallback.class)
public interface JjcheHelloApi {

    /**
     * 根据service_path获取api配置信息
     *
     * @param name /
     * @return /
     */
    @GetMapping(value = "/api/cloud/server")
    ResultWrapper<String> server(@RequestParam(value = "name") String name);
}
