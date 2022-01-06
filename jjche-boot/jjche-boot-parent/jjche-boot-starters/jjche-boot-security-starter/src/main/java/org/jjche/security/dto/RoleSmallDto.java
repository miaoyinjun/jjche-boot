package org.jjche.security.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>RoleSmallDto class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2018-11-23
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
