package org.jjche.demo.modules.a.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import org.jjche.demo.modules.a.mapper.TeacherMapper;
import org.jjche.demo.modules.a.domain.TeacherDO;
import org.jjche.common.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.jjche.demo.modules.a.api.dto.TeacherDTO;
import org.jjche.demo.modules.a.api.dto.TeacherQueryCriteriaDTO;
import org.jjche.demo.modules.a.api.vo.TeacherVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import javax.servlet.http.HttpServletResponse;
import org.jjche.common.param.PageParam;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jjche.mybatis.util.MybatisUtil;
import org.jjche.mybatis.base.service.MyServiceImpl;
import org.jjche.common.param.MyPage;
import org.jjche.demo.modules.a.mapstruct.TeacherMapStruct;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import cn.hutool.core.lang.Assert;
import org.jjche.mybatis.param.SortEnum;
import java.util.List;
/**
* <p>
* ss 服务实现类
* </p>
*
* @author miaoyj
* @since 2022-07-19
*/
@Service
@RequiredArgsConstructor
public class TeacherService extends MyServiceImpl<TeacherMapper, TeacherDO> {

    private final TeacherMapStruct teacherMapStruct;

    /**
     * <p>
     * 获取列表查询语句
     * </p>
     *
     * @param query 条件
     * @return sql
     */
    private LambdaQueryWrapper queryWrapper(TeacherQueryCriteriaDTO query) {
        return MybatisUtil.assemblyLambdaQueryWrapper(query, SortEnum.ID_DESC);
    }

   /**
   * <p>
   * 创建
   * </p>
   * @param dto 创建对象
   * @return id
   */
    @Transactional(rollbackFor = Exception.class)
    public Long save(TeacherDTO dto) {
        //唯一索引验证
        String name = dto.getName();
        LambdaQueryWrapper<TeacherDO> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(TeacherDO::getName, name);
        TeacherDO dbDO = this.getOne(queryWrapper, false);
        Assert.isTrue(dbDO == null, name + "已存在");
        TeacherDO teacherDO = this.teacherMapStruct.toDO(dto);
        Assert.isTrue(this.save(teacherDO), "保存失败");
        return teacherDO.getId();
    }

    /**
    * <p>
    * 多选删除
    * </p>
    * @param ids 主键
    */
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Long> ids){
        Assert.isTrue(this.removeBatchByIdsWithFill(new TeacherDO(), ids) == ids.size(), "删除失败，记录不存在");
    }

    /**
    * <p>
    * 编辑
    * </p>
    * @param dto 编辑对象
    */
    @Transactional(rollbackFor = Exception.class)
    public void update(TeacherDTO dto) {
        TeacherDO teacherDO = this.getById(dto.getId());
        Assert.notNull(teacherDO, "记录不存在");
        //唯一索引验证
        String name = dto.getName();
        LambdaQueryWrapper<TeacherDO> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(TeacherDO::getName, name);
        queryWrapper.ne(TeacherDO::getId, dto.getId());
        TeacherDO dbDO = this.getOne(queryWrapper, false);
        Assert.isTrue(dbDO == null, name + "已存在");
        teacherDO = this.teacherMapStruct.toDO(dto);
        Assert.isTrue(this.updateById(teacherDO), "修改失败，记录不存在");
}
    /**
    * <p>
    * 根据ID查询
    * </p>
    * @param id ID
    * @return TeacherVO 对象
    */
    public TeacherVO getVoById(Long id) {
        TeacherDO teacherDO = this.getById(id);
        Assert.notNull(teacherDO, "记录不存在");
        return this.teacherMapStruct.toVO(teacherDO);
    }

    /**
    * <p>
    * 查询数据分页
    * </p>
    * @param query 条件
    * @param page 分页
    * @return TeacherVO 分页
    */
    public MyPage<TeacherVO> page(PageParam page, TeacherQueryCriteriaDTO query) {
        LambdaQueryWrapper<TeacherDO> queryWrapper = queryWrapper(query);
        return this.baseMapper.pageQuery(page, queryWrapper);
    }

    /**
    * <p>
    * 查询所有数据不分页
    * </p>
    * @param query 条件
    * @return TeacherVO 列表对象
    */
    public List<TeacherVO> list(TeacherQueryCriteriaDTO query){
        LambdaQueryWrapper<TeacherDO> queryWrapper = queryWrapper(query);
        List<TeacherDO> list = this.list(queryWrapper);
        return teacherMapStruct.toVO(list);
    }

    /**
    * <p>
    * 导出数据
    * </p>
    * @param query 条件
    */
    public void download(TeacherQueryCriteriaDTO query) {
        List<Map<String, Object>> list = new ArrayList<>();
        List<TeacherVO> all = this.list(query);
        for (TeacherVO teacher : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("姓名", teacher.getName());
            map.put("年龄", teacher.getAge());
            map.put("课程", teacher.getCourse());
            map.put("所属用户id", teacher.getCreatorUserId());
            list.add(map);
        }
        try {
            HttpServletResponse httpServletResponse = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
            FileUtil.downloadExcel(list, httpServletResponse);
        }catch (IOException e){
            throw new IllegalArgumentException("文件下载失败");
        }
    }
}