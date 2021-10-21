package com.boot.admin.system.modules.system.mapstruct;

import com.boot.admin.system.modules.system.domain.UserDO;
import com.boot.admin.system.modules.system.dto.UserDTO;
import com.boot.admin.core.base.BaseMapStruct;
import com.boot.admin.security.dto.UserVO;
import org.mapstruct.Mapper;

/**
 * <p>UserMapStruct interface.</p>
 *
 * @author Zheng Jie
 * @since 2018-11-23
 * @version 1.0.8-SNAPSHOT
 */
@Mapper(componentModel = "spring")
public interface UserMapStruct extends BaseMapStruct<UserDO, UserDTO, UserVO> {
}
