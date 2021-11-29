package com.boot.admin.system.modules.system.api.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.io.Serializable;

/**
 * <p>MenuMetaVO class.</p>
 *
 * @author Zheng Jie
 * @since 2018-12-20
 * @version 1.0.8-SNAPSHOT
 */
@Data
@AllArgsConstructor
public class MenuMetaVO implements Serializable {

    private String title;

    private String icon;

    private Boolean noCache;
}
