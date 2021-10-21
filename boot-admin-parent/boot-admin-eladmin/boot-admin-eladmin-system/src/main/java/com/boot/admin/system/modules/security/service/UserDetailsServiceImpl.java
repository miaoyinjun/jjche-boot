package com.boot.admin.system.modules.security.service;

import com.boot.admin.system.modules.system.service.*;
import com.boot.admin.core.wrapper.constant.HttpStatusConstant;
import com.boot.admin.security.dto.UserVO;
import com.boot.admin.security.service.JwtUserService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * <p>UserDetailsServiceImpl class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2018-11-22
 */
@Service("userDetailsService")
@ConditionalOnMissingBean(type = "userDetailsService")
public class UserDetailsServiceImpl extends AbstractUserDetailsService {
    /**
     * <p>Constructor for UserDetailsServiceImpl.</p>
     *
     * @param userService a {@link com.boot.admin.system.modules.system.service.UserService} object.
     * @param roleService a {@link com.boot.admin.system.modules.system.service.RoleService} object.
     * @param dataService a {@link com.boot.admin.system.modules.system.service.DataService} object.
     * @param jwtUserService a {@link com.boot.admin.security.service.JwtUserService} object.
     * @param deptService a {@link com.boot.admin.system.modules.system.service.DeptService} object.
     * @param jobService a {@link com.boot.admin.system.modules.system.service.JobService} object.
     */
    public UserDetailsServiceImpl(UserService userService,
                                  RoleService roleService,
                                  DataService dataService,
                                  JwtUserService jwtUserService,
                                  DeptService deptService,
                                  JobService jobService) {
        super(userService, roleService, dataService, jwtUserService, deptService, jobService);
    }

    @Override
    UserVO findUserDto(String username) {
        UserVO userDto = userService.findByName(username);
        if (userDto == null) {
            throw new UsernameNotFoundException(HttpStatusConstant.MSG_USERNAME_NOTFOUND_OR_BAD_CREDENTIALS);
        }
        return userDto;
    }
}
