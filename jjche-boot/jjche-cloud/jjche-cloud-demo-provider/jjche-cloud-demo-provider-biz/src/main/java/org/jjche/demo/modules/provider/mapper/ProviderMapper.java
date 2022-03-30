package org.jjche.demo.modules.provider.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.demo.modules.provider.api.vo.ProviderVO;
import org.jjche.demo.modules.provider.domain.ProviderDO;
import org.jjche.mybatis.base.MyBaseMapper;

import java.util.List;

/**
 * <p>
 * 学生
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-02-02
 */
public interface ProviderMapper extends MyBaseMapper<ProviderDO> {

    /**
     * <p>
     * 分页查询
     * </p>
     *
     * @param page    分页
     * @param wrapper 自定义sql
     * @return 分页VO
     */
    MyPage<ProviderVO> pageQuery(PageParam page, @Param(Constants.WRAPPER) Wrapper wrapper);

    /**
     * <p>
     * 查询全部
     * </p>
     *
     * @param wrapper 自定义sql
     * @return DO
     */
    List<ProviderVO> queryAll(@Param(Constants.WRAPPER) Wrapper wrapper);
}
