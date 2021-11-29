package com.boot.admin.system.modules.system.rest;

import com.boot.admin.common.enums.CodeBiEnum;
import com.boot.admin.common.enums.CodeEnum;
import com.boot.admin.system.modules.system.service.VerifyService;
import com.boot.admin.tool.modules.tool.service.EmailService;
import com.boot.admin.tool.modules.tool.vo.EmailVO;
import com.boot.admin.core.annotation.controller.SysRestController;
import com.boot.admin.core.base.BaseController;
import com.boot.admin.core.wrapper.response.ResultWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Objects;

/**
 * <p>VerifyController class.</p>
 *
 * @author Zheng Jie
 * @since 2018-12-26
 * @version 1.0.8-SNAPSHOT
 */
@RequiredArgsConstructor
@Api(tags = "系统：验证码管理")
@SysRestController("code")
public class VerifyController extends BaseController {

    private final VerifyService verificationCodeService;
    private final EmailService emailService;

    /**
     * <p>resetEmail.</p>
     *
     * @param email a {@link java.lang.String} object.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @PostMapping(value = "/resetEmail")
    @ApiOperation("重置邮箱，发送验证码")
    public ResultWrapper resetEmail(@RequestParam String email) {
        EmailVO emailVo = verificationCodeService.sendEmail(email, CodeEnum.EMAIL_RESET_EMAIL_CODE.getKey());
        emailService.send(emailVo, emailService.find());
        return ResultWrapper.ok();
    }

    /**
     * <p>resetPass.</p>
     *
     * @param email a {@link java.lang.String} object.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @PostMapping(value = "/email/resetPass")
    @ApiOperation("重置密码，发送验证码")
    public ResultWrapper resetPass(@RequestParam String email) {
        EmailVO emailVo = verificationCodeService.sendEmail(email, CodeEnum.EMAIL_RESET_PWD_CODE.getKey());
        emailService.send(emailVo, emailService.find());
        return ResultWrapper.ok();
    }

    /**
     * <p>validated.</p>
     *
     * @param email a {@link java.lang.String} object.
     * @param code a {@link java.lang.String} object.
     * @param codeBi a {@link java.lang.Integer} object.
     * @return a {@link com.boot.admin.core.wrapper.response.ResultWrapper} object.
     */
    @GetMapping(value = "/validated")
    @ApiOperation("验证码验证")
    public ResultWrapper validated(@RequestParam String email, @RequestParam String code, @RequestParam Integer codeBi) {
        CodeBiEnum biEnum = CodeBiEnum.find(codeBi);
        switch (Objects.requireNonNull(biEnum)) {
            case ONE:
                verificationCodeService.validated(CodeEnum.EMAIL_RESET_EMAIL_CODE.getKey() + email, code);
                break;
            case TWO:
                verificationCodeService.validated(CodeEnum.EMAIL_RESET_PWD_CODE.getKey() + email, code);
                break;
            default:
                break;
        }
        return ResultWrapper.ok();
    }
}
