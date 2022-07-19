package org.jjche.demo.modules.a.domain;

import org.jjche.mybatis.base.entity.BaseEntityLogicDelete;
import org.jjche.mybatis.base.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.sql.Timestamp;
import java.math.BigDecimal;
/**
* <p>
* ss
* </p>
*
* @author miaoyj
* @since 2022-07-19
*/
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("teacher")
public class TeacherDO extends BaseEntity {
    /**
    * 姓名
    */
    private String name;
    /**
    * 年龄
    */
    private Integer age;
    /**
    * 课程
    */
    private String course;
    /**
    * 所属用户id
    */
    private Long creatorUserId;
}