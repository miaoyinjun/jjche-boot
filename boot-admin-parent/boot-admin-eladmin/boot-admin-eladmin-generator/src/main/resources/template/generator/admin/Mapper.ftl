package ${packageService}.mapper;

import ${packageService}.domain.${className}DO;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.boot.admin.mybatis.base.MyBaseMapper;
import com.boot.admin.mybatis.param.PageParam;
import ${packageApi}.vo.${className}VO;
import org.apache.ibatis.annotations.Param;
import com.boot.admin.mybatis.param.MyPage;
import java.util.List;
import ${packageApi}.enums.${className}SortEnum;

/**
* <p>
* ${apiAlias}
* </p>
*
* @author ${author}
* @since ${date}
*/
public interface ${className}Mapper extends MyBaseMapper<${className}DO> {

    /**
    * <p>
    * 分页查询
    * </p>
    *
    * @param page 分页
    * @param sort 排序
    * @param wrapper 自定义sql
    * @return 分页VO
    */
    MyPage<${className}VO> pageQuery(PageParam page, ${className}SortEnum sort, @Param(Constants.WRAPPER) Wrapper wrapper);

    /**
    * <p>
    * 查询全部
    * </p>
    *
    * @param sort 排序
    * @param wrapper 自定义sql
    * @return DO
    */
    List<${className}DO> queryAll(${className}SortEnum sort, @Param(Constants.WRAPPER) Wrapper wrapper);
}