package org.jjche.demo.modules.student.rest;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.jjche.common.annotation.HttpBodyDecrypt;
import org.jjche.common.annotation.HttpResDataEncrypt;
import org.jjche.common.constant.FilterEncConstant;
import org.jjche.common.wrapper.response.ResultWrapper;
import org.jjche.core.annotation.controller.OutRestController;
import org.jjche.core.base.BaseController;
import org.jjche.demo.modules.student.api.dto.StudentDTO;
import org.jjche.demo.modules.student.service.StudentService;
import org.jjche.security.annotation.rest.AnonymousPostMapping;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>
 * 学生-对外 控制器
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-02-02
 */
@Api(tags = "对外-加密|解密")
@ApiSupport(order = 1, author = "miaoyj")
@OutRestController("students")
@RequiredArgsConstructor
public class OutStudentController extends BaseController {

    private final StudentService studentService;

    @ApiOperation(value = "添加-出参加密，入参解密")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = FilterEncConstant.PARAM_TYPE_HEADER, name = FilterEncConstant.APP_ID, value = FilterEncConstant.APP_ID_DESC, example = FilterEncConstant.DEFAULT_APP_ID, dataTypeClass = String.class, required = true),
            @ApiImplicitParam(paramType = FilterEncConstant.PARAM_TYPE_HEADER, name = FilterEncConstant.TIMESTAMP, value = FilterEncConstant.TIMESTAMP_DESC, dataTypeClass = String.class, required = true),
            @ApiImplicitParam(paramType = FilterEncConstant.PARAM_TYPE_HEADER, name = FilterEncConstant.NONCE, value = FilterEncConstant.NONCE_DESC, example = "1234567890", dataTypeClass = String.class, required = true),
            @ApiImplicitParam(paramType = FilterEncConstant.PARAM_TYPE_HEADER, name = FilterEncConstant.SIGN, value = FilterEncConstant.SIGN_DESC, dataTypeClass = String.class, required = true),
    })
    @HttpBodyDecrypt
    @HttpResDataEncrypt
    @AnonymousPostMapping(value = "add")
    public ResultWrapper<StudentDTO> post(@Validated @Valid @RequestBody StudentDTO dto) {
        return ResultWrapper.ok(dto);
    }
}