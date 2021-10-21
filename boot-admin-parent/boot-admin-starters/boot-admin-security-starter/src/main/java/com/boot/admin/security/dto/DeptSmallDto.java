package com.boot.admin.security.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>DeptSmallDto class.</p>
 *
 * @author Zheng Jie
 * @since 2019-6-10 16:32:18
 * @version 1.0.8-SNAPSHOT
 */
@Data
public class DeptSmallDto implements Serializable {

    private Long id;

    private String name;

    /**
     * <p>Constructor for DeptSmallDto.</p>
     */
    public DeptSmallDto() {
    }
}
