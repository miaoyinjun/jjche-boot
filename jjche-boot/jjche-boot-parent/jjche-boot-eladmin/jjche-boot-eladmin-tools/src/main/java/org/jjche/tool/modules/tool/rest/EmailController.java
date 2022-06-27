package org.jjche.tool.modules.tool.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.jjche.common.enums.LogCategoryType;
import org.jjche.common.enums.LogType;
import org.jjche.common.wrapper.response.ResultWrapper;
import org.jjche.core.annotation.controller.SysRestController;
import org.jjche.core.base.BaseController;
import org.jjche.log.biz.starter.annotation.LogRecordAnnotation;
import org.jjche.tool.modules.tool.domain.EmailConfigDO;
import org.jjche.tool.modules.tool.service.EmailService;
import org.jjche.tool.modules.tool.vo.EmailVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 发送邮件
 *
 * @author 郑杰
 * @version 1.0.8-SNAPSHOT
 * @since 2018/09/28 6:55:53
 */
@RequiredArgsConstructor
@SysRestController("email")
@Api(tags = "工具：邮件管理")
public class EmailController extends BaseController {

    private final EmailService emailService;

    /**
     * <p>queryConfig.</p>
     *
     * @return a {@link ResultWrapper} object.
     */
    @GetMapping
    public ResultWrapper<EmailConfigDO> queryConfig() {
        return ResultWrapper.ok(emailService.find());
    }

    /**
     * <p>updateConfig.</p>
     *
     * @param emailConfig a {@link EmailConfigDO} object.
     * @return a {@link ResultWrapper} object.
     * @throws java.lang.Exception if any.
     */
    @LogRecordAnnotation(
            value = "配置", category = LogCategoryType.MANAGER,
            type = LogType.UPDATE, module = "邮件"
    )
    @PutMapping
    @ApiOperation("配置邮件")
    public ResultWrapper updateConfig(@Validated @RequestBody EmailConfigDO emailConfig) throws Exception {
        emailService.config(emailConfig, emailService.find());
        return ResultWrapper.ok();
    }

    /**
     * <p>sendEmail.</p>
     *
     * @param emailVo a {@link EmailVO} object.
     * @return a {@link ResultWrapper} object.
     */
    @LogRecordAnnotation(
            value = "发送", category = LogCategoryType.MANAGER,
            type = LogType.ADD, module = "邮件"
    )
    @PostMapping
    @ApiOperation("发送邮件")
    public ResultWrapper sendEmail(@Validated @RequestBody EmailVO emailVo) {
        emailService.send(emailVo, emailService.find());
        return ResultWrapper.ok();
    }
}
