package com.boot.admin.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 验证码业务场景
 * </p>
 *
 * @author Zheng Jie
 * @since 2020-05-02
 * @version 1.0.8-SNAPSHOT
 */
@Getter
@AllArgsConstructor
public enum CodeBiEnum {

    /* 旧邮箱修改邮箱 */
    ONE(1, "旧邮箱修改邮箱"),

    /* 通过邮箱修改密码 */
    TWO(2, "通过邮箱修改密码");

    /**
     * <p>find.</p>
     *
     * @param code a {@link java.lang.Integer} object.
     * @return a {@link CodeBiEnum} object.
     */
    /**
     * <p>find.</p>
     *
     * @param code a {@link java.lang.Integer} object.
     * @return a {@link CodeBiEnum} object.
     */
    /**
     * <p>find.</p>
     *
     * @param code a {@link java.lang.Integer} object.
     * @return a {@link CodeBiEnum} object.
     */
    /**
     * <p>find.</p>
     *
     * @param code a {@link java.lang.Integer} object.
     * @return a {@link CodeBiEnum} object.
     */
    /**
     * <p>find.</p>
     *
     * @param code a {@link java.lang.Integer} object.
     * @return a {@link CodeBiEnum} object.
     */
    /**
     * <p>find.</p>
     *
     * @param code a {@link java.lang.Integer} object.
     * @return a {@link CodeBiEnum} object.
     */
    /**
     * <p>find.</p>
     *
     * @param code a {@link java.lang.Integer} object.
     * @return a {@link CodeBiEnum} object.
     */
    /**
     * <p>find.</p>
     *
     * @param code a {@link java.lang.Integer} object.
     * @return a {@link com.boot.admin.common.enums.CodeBiEnum} object.
     */
    private final Integer code;
    private final String description;

    public static CodeBiEnum find(Integer code) {
        for (CodeBiEnum value : CodeBiEnum.values()) {
            if (code.equals(value.getCode())) {
                return value;
            }
        }
        return null;
    }

}
