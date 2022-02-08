package org.jjche.demo.modules.student.feign;

import org.jjche.core.wrapper.response.ResultWrapper;
import org.jjche.demo.modules.student.api.dto.StudentDTO;
import org.jjche.demo.modules.student.api.vo.StudentVO;
import org.jjche.security.annotation.rest.AnonymousPostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * <p>
 * JjcheSysApi
 * </p>
 *
 * @author miaoyj
 * @since 2022-01-21
 */
//@FeignClient(value = "jjche-cloud-server", fallbackFactory = JjcheSysFallback.class)
public interface JjcheSysApi {

    /**
     * 根据service_path获取api配置信息
     *
     * @param name /
     * @return /
     */
    @AnonymousPostMapping(value = "/api/cloud/server")
    ResultWrapper<StudentVO> server(@RequestBody StudentDTO studentDTO);
}
