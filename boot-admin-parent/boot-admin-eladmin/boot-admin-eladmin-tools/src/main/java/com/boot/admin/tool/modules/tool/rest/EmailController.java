package com.boot.admin.tool.modules.tool.rest;

import com.boot.admin.tool.modules.tool.domain.EmailConfigDO;
import com.boot.admin.tool.modules.tool.service.EmailService;
import com.boot.admin.tool.modules.tool.vo.EmailVO;
import com.boot.admin.common.enums.LogCategoryType;
import com.boot.admin.common.enums.LogType;
import com.boot.admin.core.annotation.controller.SysRestController;
import com.boot.admin.core.base.BaseController;
import com.boot.admin.core.wrapper.response.ResultWrapper;
import com.boot.admin.log.biz.starter.annotation.LogRecordAnnotation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 发送邮件
 *
 * @author 郑杰
 * @since 2018/09/28 6:55:53
 * @version 1.0.8-SNAPSHOT
 */
@RequiredArgsConstructor
@SysRestController("email")
@Api(tags = "工具：邮件管理")
public class EmailController extends BaseController {

    private final EmailService emailService;

    /**
     * <p>queryConfig.</p>
     *
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @GetMapping
    public ResultWrapper<EmailConfigDO> queryConfig() {
        return ResultWrapper.ok(emailService.find());
    }

    /**
     * <p>updateConfig.</p>
     *
     * @param emailConfig a {@link com.boot.admin.tool.modules.tool.domain.EmailConfigDO} object.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
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
     * @param emailVo a {@link com.boot.admin.tool.modules.tool.vo.EmailVO} object.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
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
