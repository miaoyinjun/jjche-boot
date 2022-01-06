package service;

import org.jjche.security.dto.DeptSmallDto;
import org.jjche.security.dto.JwtUserDto;
import org.jjche.security.dto.UserVO;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * 用户认证
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-08-25
 */
@Service
@Configuration
public class JwtUserDetailsServiceImpl implements UserDetailsService {
    public static String username = "admin";
    public static String usernameEnabled = "admin1";
    public static String usernameAccountNonLocked = "admin2";
    public static String usernameAccountNonExpired = "admin3";
    public static String usernameCredentialsNonExpired = "admin4";

    public static String password = "admin";

    static Map<String, JwtUserDto> userDtoCache = new ConcurrentHashMap<>();

    @Override
    public JwtUserDto loadUserByUsername(String username) {
        List<GrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority("ROLE_USER"));
        String password = "$2a$10$DOCWRJejaEnhW1p7Ez4wEePbhQcdJkOOnEb17VdhMr1wQtZGAk.zi";
        UserVO user = new UserVO();
        user.setId(1L);
        user.setUsername(username);
        user.setPassword(password);
        user.setEnabled(true);
        DeptSmallDto deptSmallDto = new DeptSmallDto();
        deptSmallDto.setId(1L);
        deptSmallDto.setName("aaa");
        user.setDept(deptSmallDto);
        user.setEnabled(true);
        user.setIsCredentialsNonExpired(true);
        user.setIsAccountNonLocked(true);
        user.setIsAccountNonExpired(true);
        JwtUserDto jwtUserDto = new JwtUserDto(
                user,
                null,
                authorityList
        );
        if (username.equalsIgnoreCase(usernameEnabled)) {
            user.setEnabled(false);
        } else if (username.equalsIgnoreCase(usernameCredentialsNonExpired)) {
            user.setIsCredentialsNonExpired(false);
        } else if (username.equalsIgnoreCase(usernameAccountNonLocked)) {
            user.setIsAccountNonLocked(false);
        } else if (username.equalsIgnoreCase(usernameAccountNonExpired)) {
            user.setIsAccountNonExpired(false);
        }
        userDtoCache.put(username, jwtUserDto);
        return jwtUserDto;
    }
}