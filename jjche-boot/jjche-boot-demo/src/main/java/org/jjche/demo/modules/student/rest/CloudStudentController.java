package org.jjche.demo.modules.student.rest;

import cn.hutool.core.date.DateUtil;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.jjche.core.annotation.controller.ApiRestController;
import org.jjche.core.base.BaseController;
import org.jjche.core.wrapper.response.ResultWrapper;
import org.jjche.demo.modules.student.feign.JjcheSysApi;
import org.jjche.security.annotation.rest.AnonymousGetMapping;
import org.jjche.security.dto.UserVO;
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

    private final JjcheSysApi jjcheSysApi;

    @ApiOperation(value = "服务端")
    @AnonymousGetMapping(value = "server")
    public ResultWrapper<UserVO> server(@RequestParam(value = "name") String name) {
        UserVO userVO = new UserVO();
        userVO.setNickName(name);
        return ResultWrapper.ok(userVO);
    }

    @ApiOperation(value = "调用端")
    @AnonymousGetMapping(value = "client")
    public ResultWrapper<UserVO> client(@RequestParam(value = "name") String name) {
        return jjcheSysApi.server(name);
    }

    @ApiOperation(value = "调用端2")
    @AnonymousGetMapping(value = "client2")
    public ResultWrapper<UserVO> client2(@RequestParam(value = "name") String name) {
        UserVO userVO = new UserVO();
        userVO.setNickName(name);
        userVO.setGmtCreate(DateUtil.date().toTimestamp());
        return ResultWrapper.ok(userVO);
    }
}