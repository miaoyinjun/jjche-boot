package com.boot.admin.system.modules.system.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 用户，岗位映射表
 * </p>
 *
 * @author miaoyj
 * @since 2021-07-12
 * @version 1.0.0-SNAPSHOT
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "sys_users_jobs")
public class UserJobDO implements Serializable {

    /**
     * 用户
     */
    @TableId
    private Long userId;

    /**
     * 岗位
     */
    private Long jobId;
}
