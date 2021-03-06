package org.jjche.system.modules.system.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.StaticLog;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.RequiredArgsConstructor;
import org.jjche.cache.service.RedisService;
import org.jjche.common.dto.*;
import org.jjche.common.enums.UserTypeEnum;
import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.common.util.PwdCheckUtil;
import org.jjche.common.util.ValidationUtil;
import org.jjche.core.util.FileUtil;
import org.jjche.core.util.RequestHolder;
import org.jjche.core.util.SecurityUtil;
import org.jjche.mybatis.base.service.MyServiceImpl;
import org.jjche.mybatis.param.SortEnum;
import org.jjche.mybatis.util.MybatisUtil;
import org.jjche.property.AdminProperties;
import org.jjche.property.AliYunSmsCodeProperties;
import org.jjche.property.PasswordProperties;
import org.jjche.security.auth.sms.SmsCodeAuthenticationToken;
import org.jjche.security.property.SecurityJwtProperties;
import org.jjche.security.property.SecurityLoginProperties;
import org.jjche.security.property.SecurityProperties;
import org.jjche.security.security.TokenProvider;
import org.jjche.security.service.JwtUserService;
import org.jjche.system.modules.system.api.dto.UserDTO;
import org.jjche.system.modules.system.api.dto.UserQueryCriteriaDTO;
import org.jjche.system.modules.system.domain.*;
import org.jjche.system.modules.system.mapper.UserJobMapper;
import org.jjche.system.modules.system.mapper.UserMapper;
import org.jjche.system.modules.system.mapper.UserRoleMapper;
import org.jjche.system.modules.system.mapstruct.UserMapStruct;
import org.jjche.tool.modules.tool.service.AvatarService;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>UserService class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2018-11-23
 */
@Service
@RequiredArgsConstructor
public class UserService extends MyServiceImpl<UserMapper, UserDO> {

    private final UserJobMapper userJobMapper;
    private final UserRoleMapper userRoleMapper;
    private final UserMapStruct userMapStruct;
    private final RedisService redisService;
    private final SysBaseAPI sysBaseAPI;
    private final AvatarService avatarService;
    private final JwtUserService jwtUserService;
    private final AdminProperties adminConfig;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final SecurityProperties properties;
    private final SmsService smsService;
    private final DeptService deptService;


    /**
     * <p>
     * ????????????????????????
     * </p>
     *
     * @param criteria ??????
     * @return sql
     */
    private LambdaQueryWrapper queryWrapper(UserQueryCriteriaDTO criteria) {
        Long deptId = criteria.getDeptId();
        if (!ObjectUtils.isEmpty(deptId)) {
            criteria.getDeptIds().add(deptId);
            criteria.getDeptIds().addAll(deptService.getDeptChildren(deptService.findByPid(deptId)));
        }

        LambdaQueryWrapper queryWrapper = MybatisUtil.assemblyLambdaQueryWrapper(criteria, SortEnum.ID_DESC);
        String blurry = criteria.getBlurry();
        if (StrUtil.isNotBlank(blurry)) {
            queryWrapper.apply("(email LIKE {0} OR username LIKE {0} OR nick_name LIKE {0})", "%" + blurry + "%");
        }
        return queryWrapper;
    }

    /**
     * ????????????
     *
     * @param criteria ??????
     * @param page     ????????????
     * @return /
     */
    public MyPage<UserVO> queryAll(UserQueryCriteriaDTO criteria, PageParam page) {
        LambdaQueryWrapper queryWrapper = queryWrapper(criteria);
        MyPage myPage = this.page(page, queryWrapper);
        List<UserVO> list = userMapStruct.toVO(myPage.getRecords());
        myPage.setNewRecords(list);
        return myPage;
    }

    /**
     * ?????????????????????
     *
     * @param criteria ??????
     * @return /
     */
    public List<UserDO> queryAll(UserQueryCriteriaDTO criteria) {
        LambdaQueryWrapper queryWrapper = queryWrapper(criteria);
        return this.list(queryWrapper);
    }

    /**
     * ??????ID??????
     *
     * @param id ID
     * @return /
     */
    public UserVO findById(long id) {
        UserDO user = this.getById(id);
        ValidationUtil.isNull(user.getId(), "UserDO", "id", id);
        return userMapStruct.toVO(user);
    }

    /**
     * <p>
     * ?????????????????????
     * </p>
     *
     * @param username ?????????
     * @return ??????
     */
    public UserDO getByUsername(String username) {
        LambdaQueryWrapper<UserDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserDO::getUsername, username);
        return this.getOne(queryWrapper);
    }

    /**
     * <p>
     * ??????????????????
     * </p>
     *
     * @param email ??????
     * @return ??????
     */
    public UserDO getByEmail(String email) {
        LambdaQueryWrapper<UserDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserDO::getEmail, email);
        return this.getOne(queryWrapper);
    }

    /**
     * <p>
     * ?????????????????????
     * </p>
     *
     * @param phone ?????????
     * @return ??????
     */
    public UserDO getByPhone(String phone) {
        LambdaQueryWrapper<UserDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserDO::getPhone, phone);
        return this.getOne(queryWrapper);
    }

    /**
     * ????????????
     *
     * @param resources /
     * @param pwd       /
     */
    @Transactional(rollbackFor = Exception.class)
    public void create(UserDTO resources, String pwd) {
        String username = resources.getUsername();
        UserDO user = this.getByUsername(username);
        Assert.isNull(user, username + "?????????");

        String email = resources.getEmail();
        user = this.getByEmail(email);
        Assert.isNull(user, email + "?????????");

        String phone = resources.getPhone();
        user = this.getByPhone(phone);
        Assert.isNull(user, phone + "?????????");

        UserDO userDO = userMapStruct.toDO(resources);
        userDO.setPwdResetTime(DateUtil.date().toTimestamp());
        //????????????????????????
        Boolean newUserMustReset = adminConfig.getUser().getPassword().getNewUserMustReset();
        //???????????????????????????
        userDO.setIsMustResetPwd(newUserMustReset);
        userDO.setPassword(pwd);
        userDO.setDeptId(resources.getDept().getId());
        this.saveUser(userDO);
        resources.setId(userDO.getId());
        updateUserAndJobAndRole(resources);
    }

    /**
     * ????????????
     *
     * @param resources /
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(UserDTO resources) {
        UserDO user = this.getById(resources.getId());
        ValidationUtil.isNull(user.getId(), "UserDO", "id", resources.getId());
        UserDO user1 = this.getByUsername(resources.getUsername());
        UserDO user2 = this.getByEmail(resources.getEmail());
        UserDO user3 = this.getByPhone(resources.getPhone());

        Boolean isUserEqual = user1 != null && !user.getId().equals(user1.getId());
        Assert.isFalse(isUserEqual, resources.getUsername() + "?????????");

        isUserEqual = user2 != null && !user.getId().equals(user2.getId());
        Assert.isFalse(isUserEqual, resources.getEmail() + "?????????");

        isUserEqual = user3 != null && !user.getId().equals(user3.getId());
        Assert.isFalse(isUserEqual, resources.getPhone() + "?????????");

        // ???????????????????????????????????????????????????
        if (!resources.getEnabled()) {
            sysBaseAPI.kickOutForUsername(resources.getUsername());
        }
        user.setUsername(resources.getUsername());
        user.setEmail(resources.getEmail());
        user.setEnabled(resources.getEnabled());
        user.setPhone(resources.getPhone());
        user.setNickName(resources.getNickName());
        user.setGender(resources.getGender());
        user.setDeptId(resources.getDept().getId());
        this.updateById(user);

        updateUserAndJobAndRole(resources);
        // ????????????
        flushCache(user.getUsername());
        flushCache(user.getPhone());
        flushCache(user.getEmail());
    }

    /**
     * <p>
     * ???????????????????????????
     * </p>
     *
     * @param resources ??????
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateUserAndJobAndRole(UserDTO resources) {
        Long userId = resources.getId();
        List<Long> jobIds = resources.getJobIds();
        this.updateUserAndJob(userId, jobIds);
        List<Long> roleIds = resources.getRoleIds();
        this.updateUserAndRole(userId, roleIds);
    }

    /**
     * ????????????????????????
     *
     * @param user /
     */
    public void saveUser(UserDO user) {
        this.saveOrUpdate(user);
        // ????????????
        flushCache(user.getUsername());
        flushCache(user.getPhone());
        flushCache(user.getEmail());
    }

    /**
     * ????????????????????????
     *
     * @param resources a {@link UserDO} object.
     * @param userName  /
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateCenter(UserDO resources, String userName) {
        UserDO user = this.getById(resources.getId());
        UserDO user1 = this.getByPhone(resources.getPhone());
        Boolean isUserEqual = user1 != null && !user.getId().equals(user1.getId());
        Assert.isFalse(isUserEqual, resources.getPhone() + "?????????");

        user.setNickName(resources.getNickName());
        user.setPhone(resources.getPhone());
        user.setGender(resources.getGender());
        this.updateById(user);
    }

    /**
     * ????????????
     *
     * @param ids /
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        for (Long id : ids) {
            // ????????????
            UserVO user = findById(id);
            flushCache(user.getUsername());
            flushCache(user.getPhone());
            flushCache(user.getEmail());
            this.removeById(id);
        }
    }

    /**
     * ?????????????????????
     *
     * @param userName /
     * @return /
     */
    public UserVO findByName(String userName) {
        UserDO user = this.getByUsername(userName);
        return userMapStruct.toVO(user);
    }

    /**
     * ????????????
     *
     * @param username ?????????
     * @param pass     ??????
     */
    @Transactional(rollbackFor = Exception.class)
    public void updatePass(String username, String pass) {
        this.baseMapper.updatePass(username, pass, new Date());
        flushCache(username);
    }

    /**
     * <p>
     * ????????????????????????????????????
     * </p>
     *
     * @param username ?????????
     */
    public void updateUserMustResetPwd(String username) {
        this.baseMapper.updateUserMustResetPwd(username);
    }

    /**
     * <p>
     * ??????????????????
     * </p>
     *
     * @param pass ??????
     */
    public void checkPwd(String pass) {
        PasswordProperties pwdConf = adminConfig.getUser().getPassword();
        //?????????????????????
        //??????
        String minLength = pwdConf.getMinLength();
        String maxLength = pwdConf.getMaxLength();
        boolean isLength = PwdCheckUtil.checkPasswordLength(pass, minLength, maxLength);
        Assert.isTrue(isLength, StrUtil.format("????????????????????????{}????????????{}???", minLength, maxLength));
        //??????
        if (pwdConf.getUpperCase()) {
            boolean isUpperCase = PwdCheckUtil.checkContainUpperCase(pass);
            Assert.isTrue(isUpperCase, "??????????????????????????????");
        }

        //??????
        if (pwdConf.getLowerCase()) {
            boolean isLowerCase = PwdCheckUtil.checkContainLowerCase(pass);
            Assert.isTrue(isLowerCase, "??????????????????????????????");
        }
        //??????
        if (pwdConf.getDigit()) {
            boolean isDigit = PwdCheckUtil.checkContainDigit(pass);
            Assert.isTrue(isDigit, "????????????????????????");
        }
        //????????????
        if (pwdConf.getSpecialChar()) {
            boolean isSpecialChar = PwdCheckUtil.checkContainSpecialChar(pass);
            Assert.isTrue(isSpecialChar, "??????????????????????????????");
        }
    }

    /**
     * <p>
     * ????????????????????????
     * </p>
     *
     * @param days ??????
     * @return ??????????????????
     */
    public Integer updateAccountExpired(Integer days) {
        return this.baseMapper.updateAccountExpired(days);
    }

    /**
     * <p>
     * ????????????????????????
     * </p>
     *
     * @param days ??????
     * @return ??????????????????
     */
    public Integer updateCredentialsExpired(Integer days) {
        return this.baseMapper.updateCredentialsExpired(days);
    }

    /**
     * <p>
     * ??????????????????????????????
     * </p>
     *
     * @param days ??????
     * @return ??????????????????
     */
    public Integer updateAllUserMustResetPwd(Integer days) {
        return this.baseMapper.updateAllUserMustResetPwd(days);
    }

    /**
     * <p>
     * ??????15???????????????????????????????????????
     * </p>
     *
     * @param days ??????
     * @return ??????????????????
     */
    public Integer updateUserTipResetPwd(Integer days) {
        return this.baseMapper.updateUserTipResetPwd(days);
    }

    /**
     * <p>
     * ???????????????????????????
     * </p>
     *
     * @param phone ?????????
     * @return ??????
     */
    public UserVO findDtoByPhone(String phone) {
        UserDO user = this.getByPhone(phone);
        return userMapStruct.toVO(user);
    }

    /**
     * <p>
     * ????????????
     * </p>
     *
     * @param authenticationToken ????????????
     * @param userTypeEnum        ????????????
     * @return ????????????
     */
    public LoginVO loginByAuthenticationToken(AbstractAuthenticationToken authenticationToken, UserTypeEnum userTypeEnum) {
        HttpServletRequest request = RequestHolder.getHttpServletRequest();
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // ????????????
        String token = tokenProvider.createToken(authentication.getName(), userTypeEnum);
        final JwtUserDto jwtUserDto = (JwtUserDto) authentication.getPrincipal();
        // ??????????????????
        sysBaseAPI.save(jwtUserDto, token, request);
        // ?????? token ??? ????????????
        SecurityJwtProperties securityJwtProperties = properties.getJwt();
        LoginVO loginVO = new LoginVO(jwtUserDto, securityJwtProperties.getTokenStartWith() + token);
        SecurityLoginProperties loginProperties = properties.getLogin();
        if (loginProperties.isSingle()) {
            //???????????????????????????token
            sysBaseAPI.checkLoginOnUser(authentication.getName(), token);
        }
        this.updateLastLoginTimeAndCleanPwdFailsCount(loginVO.getUser().getUser().getId());
        return loginVO;
    }

    /**
     * ????????????
     *
     * @param multipartFile ??????
     * @return /
     */
    @Transactional(rollbackFor = Exception.class)
    public String updateAvatar(MultipartFile multipartFile) {
        UserDO user = this.getByUsername(SecurityUtil.getUsername());
        String username = user.getUsername();
        String oldPath = user.getAvatarPath();
        File file = avatarService.uploadAvatar(multipartFile);
        user.setAvatarPath(Objects.requireNonNull(file).getPath());
        user.setAvatarName(file.getName());
        this.updateById(user);
        if (StrUtil.isNotBlank(oldPath)) {
            FileUtil.del(oldPath);
        }
        flushCache(username);
        return file.getName();
    }

    /**
     * ????????????
     *
     * @param username ?????????
     * @param email    ??????
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateEmail(String username, String email) {
        this.baseMapper.updateEmail(username, email);
        flushCache(username);
        flushCache(email);
    }

    /**
     * <p>
     * ???????????????????????????????????????????????????????????????
     * </p>
     *
     * @param userId ??????id
     * @author miaoyj
     * @since 2020-11-11
     */
    public void updateLastLoginTimeAndCleanPwdFailsCount(Long userId) {
        UserDO user = this.getById(userId);
        if (user != null) {
            user.setPwdFailsCount(0);
            user.setLastLoginTime(DateUtil.date().toTimestamp());
            this.updateById(user);
        }
    }

    /**
     * ????????????
     *
     * @param queryAll ??????????????????
     * @param response /
     * @throws java.io.IOException if any.
     */
    public void download(List<UserDO> queryAll, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (UserDO userDTO : queryAll) {
            List<String> roles = userDTO.getRoles().stream().map(RoleDO::getName).collect(Collectors.toList());
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("?????????", userDTO.getUsername());
            map.put("??????", roles);
            map.put("??????", userDTO.getDept().getName());
            map.put("??????", userDTO.getJobs().stream().map(JobDO::getName).collect(Collectors.toList()));
            map.put("??????", userDTO.getEmail());
            map.put("??????", userDTO.getEnabled() ? "??????" : "??????");
            map.put("????????????", userDTO.getPhone());
            map.put("?????????????????????", userDTO.getPwdResetTime());
            map.put("????????????", userDTO.getGmtCreate());
            map.put("????????????????????????", userDTO.getLastLoginTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    /**
     * ?????? ????????? ??????????????????
     *
     * @param username /
     */
    private void flushCache(String username) {
        jwtUserService.removeByUserName(username);
    }

    /**
     * <p>
     * ????????????????????????
     * </p>
     *
     * @param dto ???????????????
     * @return ??????????????????
     */
    public String getSmsCode(SmsCodeDTO dto) {
        String phone = dto.getPhone();
        // ???????????????
        String captchaCodeUuid = dto.getCaptchaCodeUuid();
        String captchaCode = redisService.stringGetString(captchaCodeUuid);
        // ???????????????
        redisService.delete(captchaCodeUuid);
        Assert.notBlank(captchaCode, "????????????????????????????????????");
        Assert.isTrue(StrUtil.equalsIgnoreCase(dto.getCaptchaCode(), captchaCode), "?????????????????????");
        UserDO user = this.getByPhone(phone);
        Assert.notNull(user, "???????????????????????????");
        if (!user.getEnabled()) {
            throw new DisabledException("");
        }
        //???????????????
        String smsCode = RandomUtil.randomNumbers(6);
        StaticLog.info("phone:{}, smsCode:{}", phone, smsCode);
        //???????????????
        AliYunSmsCodeProperties smsCodeConfig = adminConfig.getSms();
        Assert.notNull(smsCodeConfig, "??????????????????????????????");
        //????????????
        Long smsCodeTimeInterval = smsCodeConfig.getTimeInterval();
        SecurityJwtProperties securityJwtProperties = properties.getJwt();
        String uuid = IdUtil.simpleUUID();
        String redisUuid = securityJwtProperties.getCodeKey() + phone + ":" + uuid;
        // ??????
        redisService.stringSetString(redisUuid, smsCode, smsCodeTimeInterval);
        try {
            Boolean isSuccess = smsService.sendSmsValidCode(phone, smsService.getTemplateCodeLogin(), smsCode);
            StaticLog.info("phone:{}, isSuccess:{}", phone, isSuccess);
        } catch (Exception e) {
            StaticLog.error("phone:{}, isSuccess:{}", phone);
        }
        return uuid;
    }

    /**
     * ??????????????????
     *
     * @param ids /
     * @return /
     */
    public int countByJobs(Set<Long> ids) {
        return this.baseMapper.countByJobs(ids);
    }

    /**
     * <p>
     * ???????????????????????????
     * </p>
     *
     * @param userId ??????id
     * @param jobIds ??????id
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateUserAndJob(Long userId, List<Long> jobIds) {
        LambdaQueryWrapper<UserJobDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserJobDO::getUserId, userId);
        this.userJobMapper.delete(queryWrapper);
        if (CollUtil.isNotEmpty(jobIds)) {
            for (Long jobId : jobIds) {
                UserJobDO userJobDO = new UserJobDO(userId, jobId);
                this.userJobMapper.insert(userJobDO);
            }
        }
    }

    /**
     * <p>
     * ???????????????????????????
     * </p>
     *
     * @param userId  ??????id
     * @param roleIds ??????id
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateUserAndRole(Long userId, List<Long> roleIds) {
        LambdaQueryWrapper<UserRoleDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserRoleDO::getUserId, userId);
        this.userRoleMapper.delete(queryWrapper);
        if (CollUtil.isNotEmpty(roleIds)) {
            for (Long roleId : roleIds) {
                UserRoleDO userRoleDO = new UserRoleDO(userId, roleId);
                this.userRoleMapper.insert(userRoleDO);
            }
        }
    }

    /**
     * <p>
     * ?????????????????????
     * </p>
     *
     * @return /
     */
    public Long countTodayUser() {
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.apply("to_days(gmt_create) = to_days(now())");
        return this.baseMapper.selectCount(queryWrapper);
    }

    /**
     * <p>
     * ????????????
     * </p>
     *
     * @param dto ??????
     * @return /
     */
    public LoginVO smslogin(AuthUserSmsDto dto) {
        String phone = dto.getPhone();
        String uuid = dto.getSmsCodeUuid();
        SecurityJwtProperties securityJwtProperties = properties.getJwt();
        String redisUuid = securityJwtProperties.getCodeKey() + phone + ":" + uuid;
        String pCode = dto.getSmsCode();
        // ???????????????
        String code = redisService.stringGetString(redisUuid);
        Assert.notBlank(code, "????????????????????????????????????");
        boolean smsCodeEqual = StrUtil.equals(pCode, code);
        if (BooleanUtil.isFalse(smsCodeEqual)) {
            String smsCodeErrorCountKey = StrUtil.format("{}:sms_error_count", redisUuid);
            Long smsCodeErrorCount = redisService.objectGetObject(smsCodeErrorCountKey, Long.class);
            if (smsCodeErrorCount == null) {
                smsCodeErrorCount = 1L;
                //???????????????
                AliYunSmsCodeProperties smsCodeConfig = adminConfig.getSms();
                Assert.notNull(smsCodeConfig, "??????????????????????????????");
                //????????????
                Long smsCodeTimeInterval = smsCodeConfig.getTimeInterval();
                redisService.objectSetObject(smsCodeErrorCountKey, smsCodeErrorCount, smsCodeTimeInterval);
            } else {
                smsCodeErrorCount = redisService.stringIncrementLongString(smsCodeErrorCountKey, 1L);
            }
            boolean isErrorMax = smsCodeErrorCount > 3;
            if (isErrorMax) {
                redisService.delete(smsCodeErrorCountKey);
                redisService.delete(redisUuid);
            }
            Assert.isFalse(isErrorMax, "?????????????????????????????????????????????");
        }
        Assert.isTrue(smsCodeEqual, "?????????????????????");

        SmsCodeAuthenticationToken authenticationToken =
                new SmsCodeAuthenticationToken(phone);

        LoginVO loginVO = this.loginByAuthenticationToken(authenticationToken, UserTypeEnum.SMS);
        // ???????????????
        redisService.delete(redisUuid);
        return loginVO;
    }
}
