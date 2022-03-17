package org.jjche.demo.modules.provider.rest;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.jjche.common.response.response.ResultWrapper;
import org.jjche.core.annotation.controller.ApiRestController;
import org.jjche.core.base.BaseController;
import org.jjche.demo.constant.ProviderApiVersion;
import org.jjche.demo.modules.provider.api.dto.ProviderQueryCriteriaDTO;
import org.jjche.demo.modules.provider.api.enums.ProviderCourseEnum;
import org.jjche.demo.modules.provider.api.vo.ProviderVO;
import org.jjche.demo.modules.provider.feign.ProviderApi;
import org.jjche.demo.modules.provider.service.ProviderService;
import org.jjche.mybatis.param.MyPage;
import org.jjche.mybatis.param.PageParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 * 学生 控制器
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-02-02
 */
@Api(tags = "提供者")
@ApiSupport(order = 1, author = "miaoyj")
@ApiRestController("providers")
@RequiredArgsConstructor
public class ProviderController extends BaseController implements ProviderApi {

    private final ProviderService studentService;

    /**
     * <p>pageQuery.</p>
     *
     * @param page   a {@link org.jjche.mybatis.param.PageParam} object.
     * @param query  a {@link ProviderQueryCriteriaDTO} object.
     * @param course a {@link ProviderCourseEnum} object.
     * @return a {@link ResultWrapper} object.
     */
    @GetMapping
    @ApiOperation(value = "学生-列表", tags = ProviderApiVersion.VERSION_1_0_0)
    @PreAuthorize("@el.check('student:list')")
    public ResultWrapper<MyPage<ProviderVO>> page(PageParam page,
                                                  @ApiParam(value = "课程")
                                                  @RequestParam(required = false) ProviderCourseEnum course,
                                                  @Validated ProviderQueryCriteriaDTO query) {
        return ResultWrapper.ok(studentService.page(page, course, query));
    }

}
