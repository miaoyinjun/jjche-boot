package org.jjche.demo.modules.student.rest;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.jjche.core.annotation.controller.ApiRestController;
import org.jjche.core.base.BaseController;

/**
 * <p>
 * 学生 控制器
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-02-02
 */
@Api(tags = "feign调用")
@ApiSupport(order = 1, author = "miaoyj")
@ApiRestController("cloud")
@RequiredArgsConstructor
public class CloudStudentController extends BaseController {

//    private final ProviderStudentApi providerStudentApi;
//
//    @ApiOperation(value = "调用端")
//    @GetMapping(value = "client")
//    public R<MyPage<ProviderVO>> client(@SpringQueryMap PageParam page,
//                                                    @ApiParam(value = "课程")
//                                                    @RequestParam(required = false) ProviderCourseEnum course,
//                                                    @ApiParam(value = "姓名", example = "大")
//                                                        @RequestParam(required = false) String name) {
//        return providerStudentApi.page(page, course, name);
//    }
}