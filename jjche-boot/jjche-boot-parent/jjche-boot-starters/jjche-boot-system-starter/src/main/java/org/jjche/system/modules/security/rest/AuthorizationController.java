package org.jjche.system.modules.security.rest;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.wf.captcha.base.Captcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.jjche.cache.service.RedisService;
import org.jjche.common.api.CommonAPI;
import org.jjche.common.dto.AuthUserDto;
import org.jjche.common.dto.AuthUserSmsDto;
import org.jjche.common.dto.LoginVO;
import org.jjche.common.dto.SmsCodeDTO;
import org.jjche.common.enums.LogCategoryType;
import org.jjche.common.enums.LogModule;
import org.jjche.common.enums.LogType;
import org.jjche.common.enums.UserTypeEnum;
import org.jjche.common.util.RsaUtils;
import org.jjche.common.wrapper.response.ResultWrapper;
import org.jjche.core.annotation.controller.SysRestController;
import org.jjche.core.base.BaseController;
import org.jjche.log.biz.starter.annotation.LogRecordAnnotation;
import org.jjche.security.annotation.rest.AnonymousDeleteMapping;
import org.jjche.security.annotation.rest.AnonymousGetMapping;
import org.jjche.security.annotation.rest.AnonymousPostMapping;
import org.jjche.security.property.*;
import org.jjche.security.security.TokenProvider;
import org.jjche.system.modules.security.vo.LoginCodeVO;
import org.jjche.system.modules.system.service.UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>AuthorizationController class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2018-11-23
 * 授权、根据token获取用户详细信息
 */
@SysRestController("auth")
@RequiredArgsConstructor
@Api(tags = "系统：系统授权")
public class AuthorizationController extends BaseController {
    private final SecurityProperties properties;
    private final RedisService redisService;
    private final CommonAPI commonAPI;
    private final UserService userService;
    private final TokenProvider tokenProvider;

    /**
     * <p>login.</p>
     *
     * @param authUser a {@link AuthUserDto} object.
     * @return a {@link ResultWrapper} object.
     */
    @AnonymousPostMapping(value = "/login")
    @ApiOperation("密码登录授权")
    @LogRecordAnnotation(
            value = "密码登录", category = LogCategoryType.MANAGER,
            type = LogType.SELECT, module = LogModule.LOG_MODULE_LOGIN, operatorId = "{{#authUser.username}}", saveParams = false
    )
    public ResultWrapper<LoginVO> login(@Validated @RequestBody AuthUserDto authUser) {
        SecurityRsaProperties rsaProperties = properties.getRsa();
        // 密码解密
        String password = RsaUtils.decryptByPrivateKey(rsaProperties.getPrivateKey(), authUser.getPassword());
        // 查询验证码
        String code = redisService.stringGetString(authUser.getUuid());
        // 清除验证码
        redisService.delete(authUser.getUuid());
        Assert.notBlank(code, "验证码不存在或已过期");
        Assert.isFalse(StrUtil.isBlank(authUser.getCode()) || !authUser.getCode().equalsIgnoreCase(code), "验证码错误");

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(authUser.getUsername(), password);

        LoginVO loginVO = userService.loginByAuthenticationToken(authenticationToken, UserTypeEnum.PWD);
        return ResultWrapper.ok(loginVO);
    }

    /**
     * <p>smslogin.</p>
     *
     * @param dto a {@link AuthUserSmsDto} object.
     * @return a {@link ResultWrapper} object.
     */
    @AnonymousPostMapping(value = "/sms_login")
    @ApiOperation("短信登录授权")
    @LogRecordAnnotation(
            value = "短信登录", category = LogCategoryType.MANAGER,
            type = LogType.SELECT, module = LogModule.LOG_MODULE_LOGIN, operatorId = "{{#dto.phone}}", saveParams = false
    )
    public ResultWrapper<LoginVO> smsLogin(@Validated @RequestBody AuthUserSmsDto dto) {
        return ResultWrapper.ok(userService.smslogin(dto));
    }

    /**
     * <p>getSmsCode.</p>
     *
     * @param dto a {@link SmsCodeDTO} object.
     * @return a {@link ResultWrapper} object.
     */
    @ApiOperation(value = "认证-获取手机验证码")
    @AnonymousGetMapping(value = "/sms_code")
    public ResultWrapper<String> getSmsCode(@Valid SmsCodeDTO dto) {
        return ResultWrapper.ok(userService.getSmsCode(dto));
    }

    /**
     * <p>getUserInfo.</p>
     *
     * @return a {@link ResultWrapper} object.
     */
    @ApiOperation("获取用户信息")
    @GetMapping(value = "/info")
    public ResultWrapper<UserDetails> getUserInfo() {
        return ResultWrapper.ok(commonAPI.getUserDetails());
    }

    /**
     * <p>getCode.</p>
     *
     * @return a {@link ResultWrapper} object.
     */
    @ApiOperation("获取验证码")
    @AnonymousGetMapping(value = "/code")
    public ResultWrapper<LoginCodeVO> getCode() {
        // 获取运算的结果
        SecurityLoginProperties loginProperties = properties.getLogin();
        Captcha captcha = loginProperties.getCaptcha();
        SecurityJwtProperties securityJwtProperties = properties.getJwt();
        String uuid = securityJwtProperties.getCodeKey() + IdUtil.simpleUUID();
        //当验证码类型为 arithmetic时且长度 >= 2 时，captcha.text()的结果有几率为浮点型
        String captchaValue = captcha.text();
        String dotStr = ".";
        if (captcha.getCharType() - 1 == LoginCodeEnum.arithmetic.ordinal() && captchaValue.contains(dotStr)) {
            captchaValue = captchaValue.split("\\.")[0];
        }
        // 保存
        redisService.stringSetString(uuid, captchaValue, loginProperties.getLoginCode().getExpiration());
        // 验证码信息
        LoginCodeVO loginCodeVO = new LoginCodeVO(captcha.toBase64(), uuid);
        return ResultWrapper.ok(loginCodeVO);
    }

    /**
     * <p>logout.</p>
     *
     * @return a {@link ResultWrapper} object.
     */
    @ApiOperation("退出登录")
    @AnonymousDeleteMapping(value = "/logout")
    public ResultWrapper logout() {
        commonAPI.logoutOnlineUser(tokenProvider.resolveToken());
        return ResultWrapper.ok();
    }
}
