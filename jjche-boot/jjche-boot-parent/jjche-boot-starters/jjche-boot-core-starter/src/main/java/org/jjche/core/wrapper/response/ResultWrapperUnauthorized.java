package org.jjche.core.wrapper.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jjche.core.wrapper.constant.HttpStatusConstant;


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
