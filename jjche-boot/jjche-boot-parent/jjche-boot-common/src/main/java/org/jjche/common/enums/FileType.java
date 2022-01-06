package org.jjche.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 文件类型
 * </p>
 *
 * @author miaoyj
 * @version 1.0.1-SNAPSHOT
 * @since 2021-11-24
 */
@Getter
@AllArgsConstructor
public enum FileType {

    /**
     * 图片
     */
    IMAGE("image", "图片"),
    /**
     * 文档
     */
    TXT("txt", "文档"),
    /**
     * 音乐
     */
    MUSIC("music", "音乐"),
    /**
     * 视频
     */
    VIDEO("video", "视频"),
    /**
     * 其他
     */
    OTHER("other", "其他"),
    ;

    /**
     * Constant <code>MAPPINGS</code>
     */
    private static final Map<String, FileType> MAPPINGS;

    static {
        Map<String, FileType> temp = new HashMap<String, FileType>();
        for (FileType courseEnum : values()) {
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
     * @param index a Integer.
     * @return 枚举
     * @author miaoyj
     * @since 2020-07-09
     */
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static FileType resolve(String index) {
        return MAPPINGS.get(index);
    }
}
