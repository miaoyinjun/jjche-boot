package com.boot.admin.system.modules.security.service;

import cn.hutool.core.util.ObjectUtil;
import com.boot.admin.system.modules.system.service.*;
import com.boot.admin.security.dto.JwtUserDto;
import com.boot.admin.security.dto.UserVO;
import com.boot.admin.security.service.JwtUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * <p>UserDetailsServiceImpl class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2018-11-22
 */
@RequiredArgsConstructor
public abstract class AbstractUserDetailsService implements UserDetailsService {
    protected final UserService userService;
    private final RoleService roleService;
    private final DataService dataService;
    private final JwtUserService jwtUserService;
    protected final DeptService deptService;
    protected final JobService jobService;

    /**
     * <p>
     * 查询用户
     * </p>
     *
     * @param username 用户名
     * @return 用户
     */
    abstract UserVO findUserDto(String username);

    /** {@inheritDoc} */
    @Override
    public JwtUserDto loadUserByUsername(String username) {
        JwtUserDto jwtUserDto = jwtUserService.getByUserName(username);
        if (ObjectUtil.isNull(jwtUserDto)) {
            UserVO user = this.findUserDto(username);
            if (user != null) {
                jwtUserDto = new JwtUserDto(
                        user,
                        dataService.getDataScope(user),
                        roleService.mapToGrantedAuthorities(user)
                );
                jwtUserService.putByUserName(username, jwtUserDto);
            }
        }
        return jwtUserDto;
    }
}
