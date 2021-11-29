package com.boot.admin.system.modules.system.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.boot.admin.common.dto.PermissionDataRuleDTO;
import com.boot.admin.mybatis.base.MyBaseMapper;
import com.boot.admin.mybatis.param.MyPage;
import com.boot.admin.mybatis.param.PageParam;
import com.boot.admin.system.modules.system.api.enums.DataPermissionRuleSortEnum;
import com.boot.admin.system.modules.system.api.vo.DataPermissionRuleVO;
import com.boot.admin.system.modules.system.domain.DataPermissionRuleDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 数据规则
 * </p>
 *
 * @author miaoyj
 * @since 2021-10-27
 * @version 1.0.1-SNAPSHOT
 */
public interface DataPermissionRuleMapper extends MyBaseMapper<DataPermissionRuleDO> {

    /**
     * <p>
     * 分页查询
     * </p>
     *
     * @param page    分页
     * @param sort    排序
     * @param wrapper 自定义sql
     * @return 分页VO
     */
    MyPage<DataPermissionRuleVO> pageQuery(PageParam page, DataPermissionRuleSortEnum sort, @Param(Constants.WRAPPER) Wrapper wrapper);

    /**
     * <p>
     * 查询全部
     * </p>
     *
     * @param sort    排序
     * @param wrapper 自定义sql
     * @return DO
     */
    List<DataPermissionRuleDO> queryAll(DataPermissionRuleSortEnum sort, @Param(Constants.WRAPPER) Wrapper wrapper);

    /**
     * <p>
     * 根据用户id查询
     * </p>
     *
     * @param userId 用户id
     * @return /
     */
    List<PermissionDataRuleDTO> selectByUserId(@Param("userId") Long userId);
}
