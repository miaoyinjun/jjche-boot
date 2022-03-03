package org.jjche.common.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>JobSmallDto class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2019-6-10 16:32:18
 */
@Data
public class JobSmallDto implements Serializable {

    private Long id;

    private String name;

    /**
     * <p>Constructor for JobSmallDto.</p>
     */
    public JobSmallDto() {
    }
}
