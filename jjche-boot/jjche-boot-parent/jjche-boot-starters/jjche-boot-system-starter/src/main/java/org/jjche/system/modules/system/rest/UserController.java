package org.jjche.system.modules.system.rest;

import cn.hutool.core.lang.Assert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.jjche.common.annotation.PermissionData;
import org.jjche.common.dto.RoleSmallDto;
import org.jjche.common.dto.UserVO;
import org.jjche.common.enums.CodeEnum;
import org.jjche.common.enums.LogCategoryType;
import org.jjche.common.enums.LogType;
import org.jjche.common.param.PageParam;
import org.jjche.common.pojo.DataScope;
import org.jjche.common.util.RsaUtils;
import org.jjche.common.wrapper.response.ResultWrapper;
import org.jjche.core.annotation.controller.SysRestController;
import org.jjche.core.base.BaseController;
import org.jjche.core.util.SecurityUtil;
import org.jjche.log.biz.starter.annotation.LogRecordAnnotation;
import org.jjche.property.AdminProperties;
import org.jjche.property.PasswordProperties;
import org.jjche.security.property.SecurityProperties;
import org.jjche.security.property.SecurityRsaProperties;
import org.jjche.system.modules.system.api.dto.UserDTO;
import org.jjche.system.modules.system.api.dto.UserQueryCriteriaDTO;
import org.jjche.system.modules.system.api.dto.UserResetPassDTO;
import org.jjche.system.modules.system.api.vo.UserPassVO;
import org.jjche.system.modules.system.domain.UserDO;
import org.jjche.system.modules.system.service.RoleService;
import org.jjche.system.modules.system.service.UserService;
import org.jjche.system.modules.system.service.VerifyService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>UserController class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2018-11-23
 */
@Api(tags = "?????????????????????")
@SysRestController("users")
@RequiredArgsConstructor
public class UserController extends BaseController {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final RoleService roleService;
    private final VerifyService verificationCodeService;
    private final SecurityProperties properties;
    private final AdminProperties adminConfig;

    /**
     * <p>download.</p>
     *
     * @param response a {@link javax.servlet.http.HttpServletResponse} object.
     * @param criteria a {@link UserQueryCriteriaDTO} object.
     * @throws java.io.IOException if any.
     */
    @ApiOperation("??????????????????")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('user:list')")
    @PermissionData(deptIdInFieldName = DataScope.F_SQL_SCOPE_NAME)
    public void download(HttpServletResponse response, UserQueryCriteriaDTO criteria) throws IOException {
        userService.download(userService.queryAll(criteria), response);
    }

    /**
     * <p>query.</p>
     *
     * @param criteria a {@link UserQueryCriteriaDTO} object.
     * @param pageable /
     * @return a {@link ResultWrapper} object.
     */
    @ApiOperation("????????????")
    @GetMapping
    @PreAuthorize("@el.check('user:list')")
    @PermissionData(deptIdInFieldName = DataScope.F_SQL_SCOPE_NAME)
    public ResultWrapper<Object> query(UserQueryCriteriaDTO criteria, PageParam pageable) {
        return ResultWrapper.ok(userService.queryAll(criteria, pageable));
    }

    /**
     * <p>create.</p>
     *
     * @param resources a {@link UserDO} object.
     * @return a {@link ResultWrapper} object.
     */
    @LogRecordAnnotation(
            value = "??????", category = LogCategoryType.MANAGER,
            type = LogType.ADD, module = "??????"
    )
    @ApiOperation("????????????")
    @PostMapping
    @PreAuthorize("@el.check('user:add')")
    public ResultWrapper create(@Validated @RequestBody UserDTO resources) {
        PasswordProperties pwdConf = adminConfig.getUser().getPassword();
        String defaultPwd = pwdConf.getDefaultVal();
        userService.create(resources, passwordEncoder.encode(defaultPwd));
        return ResultWrapper.ok(defaultPwd);
    }

    /**
     * <p>update.</p>
     *
     * @param resources a {@link UserDO} object.
     * @return a {@link ResultWrapper} object.
     */
    @LogRecordAnnotation(
            value = "??????", category = LogCategoryType.MANAGER,
            type = LogType.UPDATE, module = "??????"
    )
    @ApiOperation("????????????")
    @PutMapping
    @PreAuthorize("@el.check('user:edit')")
    public ResultWrapper update(@Validated @RequestBody UserDTO resources) {
        userService.update(resources);
        return ResultWrapper.ok();
    }

    /**
     * <p>center.</p>
     *
     * @param resources a {@link UserDO} object.
     * @return a {@link ResultWrapper} object.
     */
    @LogRecordAnnotation(
            value = "???????????????????????????", category = LogCategoryType.MANAGER,
            type = LogType.UPDATE, module = "??????"
    )
    @ApiOperation("???????????????????????????")
    @PutMapping(value = "center")
    public ResultWrapper center(@Validated @RequestBody UserDO resources) {
        Boolean isUpdateOther = !resources.getId().equals(SecurityUtil.getUserId());
        Assert.isFalse(isUpdateOther, "????????????????????????");
        String userName = SecurityUtil.getUsername();
        userService.updateCenter(resources, userName);
        return ResultWrapper.ok();
    }

    /**
     * <p>delete.</p>
     *
     * @param ids a {@link java.util.Set} object.
     * @return a {@link ResultWrapper} object.
     */
    @LogRecordAnnotation(
            value = "??????", category = LogCategoryType.MANAGER,
            type = LogType.DELETE, module = "??????"
    )
    @ApiOperation("????????????")
    @DeleteMapping
    @PreAuthorize("@el.check('user:del')")
    public ResultWrapper delete(@RequestBody Set<Long> ids) {
        for (Long id : ids) {
            Integer currentLevel = Collections.min(roleService.findByUsersId(SecurityUtil.getUserId()).stream().map(RoleSmallDto::getLevel).collect(Collectors.toList()));
            Integer optLevel = Collections.min(roleService.findByUsersId(id).stream().map(RoleSmallDto::getLevel).collect(Collectors.toList()));
            Assert.isFalse(currentLevel > optLevel, "????????????????????????????????????" + userService.findById(id).getUsername());
        }
        userService.delete(ids);
        return ResultWrapper.ok();
    }

    /**
     * <p>updatePass.</p>
     *
     * @param passVo a {@link UserPassVO} object.
     * @return a {@link ResultWrapper} object.
     * @throws java.lang.Exception if any.
     */
    @LogRecordAnnotation(
            value = "????????????", category = LogCategoryType.MANAGER,
            type = LogType.UPDATE, module = "??????", saveParams = false
    )
    @ApiOperation("????????????")
    @PostMapping(value = "/updatePass")
    public ResultWrapper updatePass(@RequestBody UserPassVO passVo) throws Exception {
        SecurityRsaProperties rsaProperties = properties.getRsa();
        String oldPass = RsaUtils.decryptByPrivateKey(rsaProperties.getPrivateKey(), passVo.getOldPass());
        String newPass = RsaUtils.decryptByPrivateKey(rsaProperties.getPrivateKey(), passVo.getNewPass());
        //??????????????????
        userService.checkPwd(newPass);
        UserVO user = userService.findByName(SecurityUtil.getUsername());
        Boolean isOldPwdError = !passwordEncoder.matches(oldPass, user.getPassword());
        Assert.isFalse(isOldPwdError, "??????????????????????????????");
        Boolean isNewPwdMatchOldPwd = passwordEncoder.matches(newPass, user.getPassword());
        Assert.isFalse(isNewPwdMatchOldPwd, "?????????????????????????????????");
        userService.updatePass(user.getUsername(), passwordEncoder.encode(newPass));
        return ResultWrapper.ok();
    }

    /**
     * <p>resetPass.</p>
     *
     * @param passDTO a {@link UserResetPassDTO} object.
     * @return a {@link ResultWrapper} object.
     */
    @LogRecordAnnotation(
            value = "????????????", category = LogCategoryType.MANAGER,
            type = LogType.UPDATE, module = "??????", saveParams = false
    )
    @ApiOperation("????????????")
    @PreAuthorize("@el.check('admin')")
    @PutMapping(value = "/resetPass")
    public ResultWrapper resetPass(@RequestBody UserResetPassDTO passDTO) {
        SecurityRsaProperties rsaProperties = properties.getRsa();
        String newPass = RsaUtils.decryptByPrivateKey(rsaProperties.getPrivateKey(), passDTO.getNewPass());
        //??????????????????
        userService.checkPwd(newPass);
        String username = passDTO.getUsername();
        userService.updatePass(username, passwordEncoder.encode(newPass));
        //????????????????????????????????????????????????????????????
        userService.updateUserMustResetPwd(username);
        return ResultWrapper.ok();
    }

    /**
     * <p>updateAvatar.</p>
     *
     * @param avatar a {@link org.springframework.web.multipart.MultipartFile} object.
     * @return a {@link ResultWrapper} object.
     */
    @LogRecordAnnotation(
            value = "????????????", category = LogCategoryType.MANAGER,
            type = LogType.UPDATE, module = "??????", saveParams = false
    )
    @PostMapping(value = "/updateAvatar")
    public ResultWrapper<String> updateAvatar(@RequestParam MultipartFile avatar) {
        return ResultWrapper.ok(userService.updateAvatar(avatar));
    }

    /**
     * <p>updateEmail.</p>
     *
     * @param code a {@link java.lang.String} object.
     * @param user a {@link UserDO} object.
     * @return a {@link ResultWrapper} object.
     * @throws java.lang.Exception if any.
     */
    @LogRecordAnnotation(
            value = "????????????", category = LogCategoryType.MANAGER,
            type = LogType.UPDATE, module = "??????", saveParams = false
    )
    @ApiOperation("????????????")
    @PostMapping(value = "/updateEmail/{code}")
    public ResultWrapper updateEmail(@PathVariable String code, @RequestBody UserDO user) throws Exception {
        SecurityRsaProperties rsaProperties = properties.getRsa();
        String password = RsaUtils.decryptByPrivateKey(rsaProperties.getPrivateKey(), user.getPassword());
        UserVO userDto = userService.findByName(SecurityUtil.getUsername());
        Boolean isPwdError = !passwordEncoder.matches(password, userDto.getPassword());
        Assert.isFalse(isPwdError, "????????????");
        verificationCodeService.validated(CodeEnum.EMAIL_RESET_EMAIL_CODE.getKey() + user.getEmail(), code);
        userService.updateEmail(userDto.getUsername(), user.getEmail());
        return ResultWrapper.ok();
    }

}
