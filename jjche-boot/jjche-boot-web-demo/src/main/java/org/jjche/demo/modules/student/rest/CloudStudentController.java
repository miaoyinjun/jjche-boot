package org.jjche.demo.modules.student.rest;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.jjche.core.annotation.controller.ApiRestController;
import org.jjche.core.base.BaseController;
import org.jjche.core.wrapper.response.ResultWrapper;
import org.jjche.demo.modules.student.feign.JjcheHelloApi;
import org.jjche.security.annotation.rest.AnonymousGetMapping;
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

    private JjcheHelloApi jjcheHelloApi;

    @ApiOperation(value = "服务端")
    @GetMapping(value = "server")
    public ResultWrapper<String> server(@RequestParam(value = "name") String name) {
        return ResultWrapper.ok(name);
    }

    @ApiOperation(value = "调用端")
    @AnonymousGetMapping(value = "client")
    public ResultWrapper<String> client(@RequestParam(value = "name") String name) {
        //TODO 测试token传递，使用了不同的redis
        return ResultWrapper.ok("sss");
    }

}