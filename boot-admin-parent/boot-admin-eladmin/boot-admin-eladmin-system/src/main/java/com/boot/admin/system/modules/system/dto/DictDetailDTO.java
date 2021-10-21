package com.boot.admin.system.modules.system.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>DictDetailDTO class.</p>
 *
 * @author Zheng Jie
 * @since 2019-04-10
 * @version 1.0.8-SNAPSHOT
 */
@Data
public class DictDetailDTO implements Serializable {

    private DictSmallDto dict;

    private Long id;

    private Long dictId;

    private String label;

    private String value;

    private Integer dictSort;
}
