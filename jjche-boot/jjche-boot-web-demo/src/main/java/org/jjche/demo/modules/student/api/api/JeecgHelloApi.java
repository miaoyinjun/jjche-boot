package org.jjche.demo.modules.student.api.api;

import org.jjche.core.wrapper.response.ResultWrapper;
import org.jjche.demo.modules.student.api.api.fallback.JeecgHelloFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "jeecg-crm", fallbackFactory = JeecgHelloFallback.class)
public interface JeecgHelloApi {

    /**
     * 根据service_path获取api配置信息
     *
     * @param name /
     * @return /
     */
    @GetMapping(value = "/api/students/hello")
    ResultWrapper<String> getMessage(@RequestParam(value = "name") String name);
}
