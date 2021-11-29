package com.boot.admin.common.enums;

import cn.hutool.core.util.EnumUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

/**
 * <p>
 * 数据权限枚举
 * </p>
 *
 * @author Zheng Jie
 * @since 2020-05-07
 * @version 1.0.8-SNAPSHOT
 */
@Getter
@AllArgsConstructor
public enum DataScopeEnum {

    /* 全部的数据权限 */
    DATA_SCOPE_ALL("全部", "全部的数据权限"),

    /* 所在机构及以下数据 */
    DATA_SCOPE_DEPT_AND_CHILD("所在机构及以下", "所在机构及以下数据"),

    /* 自己部门的数据权限 */
    DATA_SCOPE_DEPT("所在机构", "自己部门的数据权限"),

    /* 自己创建的数据权限 */
    DATA_SCOPE_SELF("本人", "自己创建的数据权限"),

    /* 自定义的数据权限 */
    DATA_SCOPE_CUSTOM("自定义", "自定义的数据权限");

    /**
     * <p>find.</p>
     *
     * @param val a {@link java.lang.String} object.
     * @return a {@link DataScopeEnum} object.
     */
    /**
     * <p>find.</p>
     *
     * @param val a {@link java.lang.String} object.
     * @return a {@link DataScopeEnum} object.
     */
    /**
     * <p>find.</p>
     *
     * @param val a {@link java.lang.String} object.
     * @return a {@link DataScopeEnum} object.
     */
    /**
     * <p>find.</p>
     *
     * @param val a {@link java.lang.String} object.
     * @return a {@link DataScopeEnum} object.
     */
    /**
     * <p>find.</p>
     *
     * @param val a {@link java.lang.String} object.
     * @return a {@link DataScopeEnum} object.
     */
    /**
     * <p>find.</p>
     *
     * @param val a {@link java.lang.String} object.
     * @return a {@link DataScopeEnum} object.
     */
    /**
     * <p>find.</p>
     *
     * @param val a {@link java.lang.String} object.
     * @return a {@link DataScopeEnum} object.
     */
    /**
     * <p>find.</p>
     *
     * @param val a {@link java.lang.String} object.
     * @return a {@link com.boot.admin.common.enums.DataScopeEnum} object.
     */
    /**
     * <p>find.</p>
     *
     * @param val a {@link java.lang.String} object.
     * @return a {@link com.boot.admin.common.enums.DataScopeEnum} object.
     */
    /**
     * <p>find.</p>
     *
     * @param val a {@link java.lang.String} object.
     * @return a {@link com.boot.admin.common.enums.DataScopeEnum} object.
     */
    private final String value;
    private final String desc;

    public static DataScopeEnum find(String val) {
        Map<String,DataScopeEnum> enumMap = EnumUtil.getEnumMap(DataScopeEnum.class);
        return enumMap.get(val);
    }

}
