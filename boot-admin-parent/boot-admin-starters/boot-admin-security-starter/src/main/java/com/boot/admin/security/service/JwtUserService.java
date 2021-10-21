package com.boot.admin.security.service;

import com.boot.admin.security.dto.JwtUserDto;

/**
 * <p>
 * jwt用户
 * </p>
 *
 * @author miaoyj
 * @since 2020-10-20
 * @version 1.0.8-SNAPSHOT
 */
public interface JwtUserService {
    /**
     * <p>
     * 根据用户名获取jwt缓存
     * </p>
     *
     * @param userName 用户名
     * @return jwt缓存
     * @author miaoyj
     * @since 2020-10-20
     */
    JwtUserDto getByUserName(String userName);

    /**
     * <p>
     * 存入jwt缓存
     * </p>
     *
     * @param userName 用户名
     * @param jwtUserDto jwt缓存
     * @author miaoyj
     * @since 2020-10-20
     */
    void putByUserName(String userName, JwtUserDto jwtUserDto);

    /**
     * <p>
     * 删除jwt缓存
     * </p>
     *
     * @param userName 用户名
     * @author miaoyj
     * @since 2020-10-20
     */
    void removeByUserName(String userName);

}
