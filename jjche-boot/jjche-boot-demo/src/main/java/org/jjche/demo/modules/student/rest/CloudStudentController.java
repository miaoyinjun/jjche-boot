package org.jjche.demo.modules.student.rest;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.common.response.response.ResultWrapper;
import org.jjche.core.annotation.controller.ApiRestController;
import org.jjche.core.base.BaseController;
import org.jjche.demo.modules.provider.api.enums.ProviderCourseEnum;
import org.jjche.demo.modules.provider.api.vo.ProviderVO;
import org.jjche.demo.modules.provider.feign.ProviderStudentApi;
import org.springframework.cloud.openfeign.SpringQueryMap;
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
@Api(tags = "cloud")
@ApiSupport(order = 1, author = "miaoyj")
@ApiRestController("cloud")
@RequiredArgsConstructor
public class CloudStudentController extends BaseController {

    private final ProviderStudentApi providerStudentApi;

    @ApiOperation(value = "调用端")
    @GetMapping(value = "client")
    public ResultWrapper<MyPage<ProviderVO>> client(@SpringQueryMap PageParam page,
                                                    @ApiParam(value = "课程")
                                                    @RequestParam(required = false) ProviderCourseEnum course,
                                                    @RequestParam(required = false) String name) {
        return providerStudentApi.page(page, course, name);
    }
}