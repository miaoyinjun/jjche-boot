package org.jjche.cloud.modules.system.api.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * JjcheCloudEnum
 * </p>
 *
 * @author miaoyj
 * @since 2022-01-26
 */
@Getter
@AllArgsConstructor
public enum JjcheCloudEnum {

    /**
     * 测试
     */
    TEST("1", "测试"),
    ;

    /**
     * Constant <code>MAPPINGS</code>
     */
    private static final Map<String, JjcheCloudEnum> MAPPINGS;

    static {
        Map<String, JjcheCloudEnum> temp = new HashMap<String, JjcheCloudEnum>();
        for (JjcheCloudEnum courseEnum : values()) {
            temp.put(courseEnum.value, courseEnum);
        }
        MAPPINGS = Collections.unmodifiableMap(temp);
    }

    @JsonValue
    @EnumValue
    private final String value;
    private final String desc;

    /**
     * <p>
     * 根据index获取枚举
     * </p>
     *
     * @param index a Integer.
     * @return 枚举
     * @author miaoyj
     * @since 2020-07-09
     */
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static JjcheCloudEnum resolve(String index) {
        return MAPPINGS.get(index);
    }
}
