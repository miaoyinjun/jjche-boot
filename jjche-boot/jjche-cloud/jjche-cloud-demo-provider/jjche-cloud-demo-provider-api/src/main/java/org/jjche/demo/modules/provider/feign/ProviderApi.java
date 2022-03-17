package org.jjche.demo.modules.provider.feign;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.jjche.common.response.response.ResultWrapper;
import org.jjche.demo.constant.ProviderApiVersion;
import org.jjche.demo.modules.provider.api.dto.ProviderQueryCriteriaDTO;
import org.jjche.demo.modules.provider.api.enums.ProviderCourseEnum;
import org.jjche.demo.modules.provider.api.vo.ProviderVO;
import org.jjche.demo.modules.provider.feign.fallback.ProviderApiFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
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
@FeignClient(value = ProviderApiVersion.FEIGN_NAME, fallbackFactory = ProviderApiFallback.class)
public interface ProviderApi {

    @GetMapping
    @ApiOperation(value = "学生-列表", tags = ProviderApiVersion.VERSION_1_0_0)
    @PreAuthorize("@el.check('student:list')")
    public ResultWrapper<MyPage<ProviderVO>> page(PageParam page,
                                                  @ApiParam(value = "课程")
                                                  @RequestParam(required = false) ProviderCourseEnum course,
                                                  @Validated ProviderQueryCriteriaDTO query);
}
