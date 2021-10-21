package com.boot.admin.demo.modules.student.api.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 学生 排序枚举
 * </p>
 *
 * @author miaoyj
 * @since 2021-02-02
 * @version 1.0.0-SNAPSHOT
 */
@Getter
@AllArgsConstructor
public enum StudentSortEnum {

    /**
    * 主键
    */
    ID_DESC("id DESC", "id倒序"),
    ID_ASC("id ASC", "id正序"),
    ;

    /**
    * Constant <code>MAPPINGS</code>
    */
    private static final Map<String, StudentSortEnum> MAPPINGS;

    static {
        Map<String, StudentSortEnum> temp = new HashMap<String, StudentSortEnum>();
        for (StudentSortEnum courseEnum : values()) {
        temp.put(courseEnum.value, courseEnum);
        }
        MAPPINGS = Collections.unmodifiableMap(temp);
    }

    @JsonValue
    private final String value;
    private final String desc;

    /**
     * <p>
     * 根据index获取枚举
     * </p>
     *
     * @param index a String.
     * @return 枚举
     * @author miaoyj
     * @since 2021-02-02
     */
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static StudentSortEnum resolve(String index) {
    return MAPPINGS.get(index);
    }
}
