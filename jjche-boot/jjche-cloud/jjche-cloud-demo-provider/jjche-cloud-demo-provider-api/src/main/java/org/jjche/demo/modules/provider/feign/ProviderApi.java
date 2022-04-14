package org.jjche.demo.modules.provider.feign;

import org.jjche.common.param.MyPage;
import org.jjche.common.response.response.ResultWrapper;
import org.jjche.demo.constant.ProviderApiVersion;
import org.jjche.demo.modules.provider.api.enums.ProviderCourseEnum;
import org.jjche.demo.modules.provider.api.vo.ProviderVO;
import org.jjche.demo.modules.provider.feign.fallback.ProviderApiFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 * ProviderApi
 * </p>
 *
 * @author miaoyj
 * @since 2022-03-16
 */
@FeignClient(path = "/api/providers/",
        value = ProviderApiVersion.FEIGN_NAME,
        fallbackFactory = ProviderApiFallback.class
)
public interface ProviderApi {

    @GetMapping
    ResultWrapper<MyPage<ProviderVO>> page(@RequestParam Long pageIndex,
                                           @RequestParam Long pageSize,
                                           @RequestParam(required = false) ProviderCourseEnum course,
                                           @RequestParam(required = false) String name);
}
