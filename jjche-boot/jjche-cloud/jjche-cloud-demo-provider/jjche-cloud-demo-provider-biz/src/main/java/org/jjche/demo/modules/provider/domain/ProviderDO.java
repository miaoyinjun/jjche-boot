package org.jjche.demo.modules.provider.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.jjche.demo.modules.provider.api.enums.ProviderCourseEnum;
import org.jjche.mybatis.base.entity.BaseEntity;

/**
 * <p>
 * 学生
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-02-02
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("student")
public class ProviderDO extends BaseEntity {
    /**
     * 姓名
     */
    private String name;
    /**
     * 年龄
     */
    private Integer age;
    /**
     * 课程类型,102:图文,103:音频,104:视频,105:外链
     */
    private ProviderCourseEnum course;
    /**
     * 所属学生id
     */
    private Long creatorUserId;
}
