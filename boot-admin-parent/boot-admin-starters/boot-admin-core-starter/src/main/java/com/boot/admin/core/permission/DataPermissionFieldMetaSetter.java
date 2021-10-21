package com.boot.admin.core.permission;

import java.util.List;

/**
 * <p>
 * 设置数据结构
 * </p>
 *
 * @author miaoyj
 * @since 2020-11-11
 * @version 1.0.10-SNAPSHOT
 */
public interface DataPermissionFieldMetaSetter {

    /**
     * 设置数据结构，用于前台展示
     *
     * @param dataResources 数据结构
     */
    void setMeta(List<DataPermissionFieldResultVO> dataResources);

    /**
     * 获取数据结构
     *
     * @return 数据结构
     */
    List<DataPermissionFieldResultVO> getMeta();
}
