package com.boot.admin.core.wrapper.response;

import com.boot.admin.core.wrapper.constant.HttpStatusConstant;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * <p>
 * response包装未授权操作类
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-07-09
 */
@Data
public class ResultWrapperUnauthorized<T> extends ResultWrapper<T> {

    /**
     * 编号
     */
    @ApiModelProperty(value = HttpStatusConstant.CODE_TOKEN_ERROR
            + ":" + HttpStatusConstant.MSG_TOKEN_ERROR + ";"
            ,
            example = HttpStatusConstant.CODE_TOKEN_ERROR + "")
    private int code;

    @ApiModelProperty(example = HttpStatusConstant.MSG_TOKEN_ERROR + "")
    private String message;

}
