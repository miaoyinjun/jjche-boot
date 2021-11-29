package com.boot.admin.system.modules.system.api.dto;

import com.boot.admin.security.dto.DeptSmallDto;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>UserDO class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2018-11-22
 */
@Data
public class UserDTO implements Serializable {

    private Long id;

    private List<Long> roleIds;

    private List<Long> jobIds;

    private DeptSmallDto dept;

    private String username;

    private String nickName;

    private String email;

    private String phone;

    private String gender;

    private String avatarName;

    private Boolean enabled;
}
