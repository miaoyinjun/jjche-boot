package org.jjche.demo.modules.a.mapper;

import org.jjche.demo.modules.a.domain.TeacherDO;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.jjche.mybatis.base.MyBaseMapper;
import org.jjche.common.param.PageParam;
import org.jjche.demo.modules.a.api.vo.TeacherVO;
import org.apache.ibatis.annotations.Param;
import org.jjche.common.param.MyPage;
import java.util.List;

/**
* <p>
* ss
* </p>
*
* @author miaoyj
* @since 2022-07-19
*/
public interface TeacherMapper extends MyBaseMapper<TeacherDO> {

    /**
    * <p>
    * 分页查询
    * </p>
    *
    * @param page 分页
    * @param wrapper 自定义sql
    * @return 分页VO
    */
    MyPage<TeacherVO> pageQuery(PageParam page, @Param(Constants.WRAPPER) Wrapper wrapper);
}