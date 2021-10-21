package com.boot.admin.tool.modules.tool.dto;

import lombok.Data;

import java.util.List;

/**
 * sm.ms图床
 *
 * @author Zheng Jie
 * @since 2019-6-4 09:52:09
 * @version 1.0.8-SNAPSHOT
 */
@Data
public class PictureQueryCriteriaDTO {

    private String filename;
    
    private String username;

    private List<String> gmtCreate;
}
