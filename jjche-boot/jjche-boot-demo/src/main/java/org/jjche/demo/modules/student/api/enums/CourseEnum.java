package org.jjche.demo.modules.student.api.enums;

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
 * 课程类型,102:图文,103:音频,104:视频,105:外链
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-07-09
 */
@Getter
@AllArgsConstructor
public enum CourseEnum {

    /**
     * 图文
     */
    PICTURE("102", "图文"),
    /**
     * 音频
     */
    AUDIO("103", "音频"),
    /**
     * 视频
     */
    VIDEO("104", "视频"),
    /**
     * 外链
     */
    URL("105", "外链"),
    ;

    /**
     * Constant <code>MAPPINGS</code>
     */
    private static final Map<String, CourseEnum> MAPPINGS;

    static {
        Map<String, CourseEnum> temp = new HashMap<String, CourseEnum>();
        for (CourseEnum courseEnum : values()) {
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
    public static CourseEnum resolve(String index) {
        return MAPPINGS.get(index);
    }
}
