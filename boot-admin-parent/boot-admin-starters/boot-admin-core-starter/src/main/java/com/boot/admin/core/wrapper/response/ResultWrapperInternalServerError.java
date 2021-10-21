package com.boot.admin.core.wrapper.response;

import com.boot.admin.core.wrapper.constant.HttpStatusConstant;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * <p>
 * response包装内部错误操作类
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-07-09
 */
@Data
public class ResultWrapperInternalServerError<T> extends ResultWrapper<T> {

    /**
     * 编号
     */
    @ApiModelProperty(value = HttpStatusConstant.CODE_UNKNOWN_ERROR
            + ":" + HttpStatusConstant.MSG_UNKNOWN_ERROR + ";",
            example = HttpStatusConstant.CODE_UNKNOWN_ERROR + "")
    private int code;

    @ApiModelProperty(example = HttpStatusConstant.MSG_UNKNOWN_ERROR + "")
    private String message;

}
