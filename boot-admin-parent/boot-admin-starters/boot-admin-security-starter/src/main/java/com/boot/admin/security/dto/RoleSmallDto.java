package com.boot.admin.security.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>RoleSmallDto class.</p>
 *
 * @author Zheng Jie
 * @since 2018-11-23
 * @version 1.0.8-SNAPSHOT
 */
@Data
public class RoleSmallDto implements Serializable {

    private Long id;

    private String name;

    private Integer level;

    private String dataScope;

    /**
     * <p>Constructor for RoleSmallDto.</p>
     */
    public RoleSmallDto() {
    }
}
