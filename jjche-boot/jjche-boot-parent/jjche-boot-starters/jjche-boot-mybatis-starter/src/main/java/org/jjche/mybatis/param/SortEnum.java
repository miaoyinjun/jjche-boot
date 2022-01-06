package org.jjche.mybatis.param;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 排序枚举
 * </p>
 *
 * @author miaoyj
 * @since 2021-12-08
 */
@Getter
@AllArgsConstructor
public enum SortEnum {

    /**
     * 主键
     */
    ID_DESC("id DESC", "id倒序"),
    ID_ASC("id ASC", "id正序"),
    ;

    /**
     * Constant <code>MAPPINGS</code>
     */
    private static final Map<String, SortEnum> MAPPINGS;

    static {
        Map<String, SortEnum> temp = new HashMap<String, SortEnum>();
        for (SortEnum courseEnum : values()) {
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
    public static SortEnum resolve(String index) {
        return MAPPINGS.get(index);
    }
}
