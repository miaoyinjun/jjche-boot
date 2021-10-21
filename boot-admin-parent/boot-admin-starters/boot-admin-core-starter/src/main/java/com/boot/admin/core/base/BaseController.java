package com.boot.admin.core.base;

import cn.hutool.http.HttpStatus;
import com.boot.admin.core.wrapper.constant.HttpStatusConstant;
import com.boot.admin.core.wrapper.response.ResultWrapperBadRequest;
import com.boot.admin.core.wrapper.response.ResultWrapperForbidden;
import com.boot.admin.core.wrapper.response.ResultWrapperInternalServerError;
import com.boot.admin.core.wrapper.response.ResultWrapperUnauthorized;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.validation.annotation.Validated;

@ApiResponses({
        /**增加HttpStatus.HTTP_OK，会导致200的pojo swagger说明不会生成*/
//        @ApiResponse(code = HttpStatus.HTTP_OK,
//                message = "", response = ResultWrapper.class),
        @ApiResponse(code = HttpStatus.HTTP_INTERNAL_ERROR,
                message = HttpStatusConstant.HTTP_INTERNAL_ERROR, response = ResultWrapperInternalServerError.class),
        @ApiResponse(code = HttpStatus.HTTP_BAD_REQUEST,
                message = HttpStatusConstant.HTTP_BAD_REQUEST, response = ResultWrapperBadRequest.class),
        @ApiResponse(code = HttpStatus.HTTP_UNAUTHORIZED,
                message = HttpStatusConstant.MSG_TOKEN_ERROR, response = ResultWrapperUnauthorized.class),
        @ApiResponse(code = HttpStatus.HTTP_FORBIDDEN,
                message = HttpStatusConstant.HTTP_FORBIDDEN, response = ResultWrapperForbidden.class)
})
@Validated
/**
 * <p>
 * 基础 控制器
 * </p>
 *
 * @author miaoyj
 * @since 2020-09-10
 * @version 1.0.8-SNAPSHOT
 */
public class BaseController {}
