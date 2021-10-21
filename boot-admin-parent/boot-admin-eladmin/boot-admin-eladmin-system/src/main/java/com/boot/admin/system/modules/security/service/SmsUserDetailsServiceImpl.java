package com.boot.admin.system.modules.security.service;

import cn.hutool.core.lang.Assert;
import com.boot.admin.system.modules.system.service.*;
import com.boot.admin.security.dto.UserVO;
import com.boot.admin.security.service.JwtUserService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 手机号登录
 * </p>
 *
 * @author miaoyj
 * @since 2021-05-11
 * @version 1.0.0-SNAPSHOT
 */
@Service("smsUserDetailsService")
@ConditionalOnMissingBean(type = "smsUserDetailsService")
public class SmsUserDetailsServiceImpl extends AbstractUserDetailsService {

    /**
     * <p>Constructor for SmsUserDetailsServiceImpl.</p>
     *
     * @param userService a {@link com.boot.admin.system.modules.system.service.UserService} object.
     * @param roleService a {@link com.boot.admin.system.modules.system.service.RoleService} object.
     * @param dataService a {@link com.boot.admin.system.modules.system.service.DataService} object.
     * @param jwtUserService a {@link com.boot.admin.security.service.JwtUserService} object.
     * @param deptService a {@link com.boot.admin.system.modules.system.service.DeptService} object.
     * @param jobService a {@link com.boot.admin.system.modules.system.service.JobService} object.
     */
    public SmsUserDetailsServiceImpl(UserService userService,
                                     RoleService roleService,
                                     DataService dataService,
                                     JwtUserService jwtUserService,
                                     DeptService deptService,
                                     JobService jobService) {
        super(userService, roleService, dataService, jwtUserService, deptService, jobService);
    }

    @Override
    UserVO findUserDto(String username) {
        UserVO user = userService.findDtoByPhone(username);
        Assert.notNull(user, "未找到该手机号用户");
        //重置用户名为手机号
        user.setUsername(username);
        return user;
    }
}
