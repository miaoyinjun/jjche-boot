package org.jjche.security.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.jjche.common.pojo.DataScope;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>JwtUserDto class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2018-11-23
 */
@Data
public class JwtUserDto implements UserDetails {

    private final UserVO user;

    private final DataScope dataScope;
    @JsonIgnore
    private final List<GrantedAuthority> authorities;

    /**
     * <p>Constructor for JwtUserDto.</p>
     *
     * @param user        a {@link UserVO} object.
     * @param dataScope   a {@link java.util.List} object.
     * @param authorities a {@link java.util.List} object.
     */
    public JwtUserDto(UserVO user, DataScope dataScope, List<GrantedAuthority> authorities) {
        this.user = user;
        this.dataScope = dataScope;
        this.authorities = authorities;
    }

    /**
     * <p>getRoles.</p>
     *
     * @return a {@link java.util.Set} object.
     */
    public Set<String> getRoles() {
        return authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsonIgnore
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsonIgnore
    public String getUsername() {
        return user.getUsername();
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return user.getIsAccountNonExpired();
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return user.getIsAccountNonLocked();
    }

    /**
     * {@inheritDoc}
     */
    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return user.getIsCredentialsNonExpired();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return user.getEnabled();
    }
}
