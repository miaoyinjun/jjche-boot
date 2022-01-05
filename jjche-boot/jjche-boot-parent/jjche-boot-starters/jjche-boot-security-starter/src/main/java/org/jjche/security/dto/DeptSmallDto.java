package org.jjche.security.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>DeptSmallDto class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2019-6-10 16:32:18
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
