package com.boot.admin.system.modules.system.service;

import cn.hutool.core.lang.Assert;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.Cached;
import com.alicp.jetcache.anno.CreateCache;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.boot.admin.common.constant.CacheKey;
import com.boot.admin.common.util.ValidationUtil;
import com.boot.admin.system.modules.system.domain.DictDO;
import com.boot.admin.system.modules.system.domain.DictDetailDO;
import com.boot.admin.system.modules.system.api.dto.DictDetailDTO;
import com.boot.admin.system.modules.system.api.dto.DictDetailQueryCriteriaDTO;
import com.boot.admin.system.modules.system.api.dto.DictSmallDto;
import com.boot.admin.system.modules.system.mapper.DictDetailMapper;
import com.boot.admin.system.modules.system.mapstruct.DictDetailMapStruct;
import com.boot.admin.common.util.StrUtil;
import com.boot.admin.mybatis.base.service.MyServiceImpl;
import com.boot.admin.mybatis.param.MyPage;
import com.boot.admin.mybatis.param.PageParam;
import com.boot.admin.mybatis.util.MybatisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>DictDetailService class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2019-04-10
 */
@Service
@RequiredArgsConstructor
public class DictDetailService extends MyServiceImpl<DictDetailMapper, DictDetailDO> {

    private final DictService dictService;
    private final DictDetailMapStruct dictDetailMapper;
    @CreateCache(name = CacheKey.DIC_NAME)
    private Cache<String, List<DictDetailDTO>> dicDetailCache;

    /**
     * 分页查询
     *
     * @param criteria 条件
     * @param pageable 分页参数
     * @return /
     */
    public MyPage queryAll(DictDetailQueryCriteriaDTO criteria, PageParam pageable) {
        String dictName = criteria.getDictName();
        //非管理页面请求
        if (StrUtil.isNotBlank(dictName)) {
            DictDO dictDO = dictService.getByName(dictName);
            if(dictDO != null) {
                criteria.setDictId(dictDO.getId());
            }else{
                criteria.setDictId(0L);
            }
        }
        QueryWrapper queryWrapper = MybatisUtil.assemblyQueryWrapper(criteria);
        queryWrapper.orderByAsc("dict_sort");
        MyPage<DictDetailDO> myPage = this.page(pageable, queryWrapper);
        List<DictDetailDTO> list = dictDetailMapper.toVO(myPage.getRecords());
        Long dictId = criteria.getDictId();
        if(dictId != null){
            DictSmallDto dto = new DictSmallDto();
            dto.setId(dictId);
            for (DictDetailDTO detailDTO : list) {
                detailDTO.setDict(dto);
            }
        }
        myPage.setNewRecords(list);
        return myPage;
    }

    /**
     * 创建
     *
     * @param resources /
     */
    @Transactional(rollbackFor = Exception.class)
    public void create(DictDetailDO resources) {
        this.save(resources);
        // 清理缓存
        delCaches(resources);
    }

    /**
     * 编辑
     *
     * @param resources /
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(DictDetailDO resources) {
        Long id = resources.getId();
        DictDetailDO dictDetail = this.getById(id);
        ValidationUtil.isNull(dictDetail, "DictDetailDO", "id", resources.getId());
        resources.setId(id);
        this.updateById(resources);
        // 清理缓存
        delCaches(resources);
    }

    /**
     * 根据字典名称获取字典详情
     *
     * @param name 字典名称
     * @return /
     */
    @Cached(name = CacheKey.DIC_NAME, key = "#name")
    public List<DictDetailDTO> getDictByName(String name) {
        DictDO dictDO = dictService.getByName(name);
        Assert.notNull(dictDO, "未找到字典");
        Long dictId = dictDO.getId();
        LambdaQueryWrapper<DictDetailDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DictDetailDO::getDictId, dictId);
        return dictDetailMapper.toVO(this.list(queryWrapper));
    }

    /**
     * 删除
     *
     * @param id /
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        DictDetailDO dictDetail = this.getById(id);
        // 清理缓存
        delCaches(dictDetail);
        this.removeById(id);
    }

    /**
     * <p>delCaches.</p>
     *
     * @param dictDetail a {@link com.boot.admin.system.modules.system.domain.DictDetailDO} object.
     */
    public void delCaches(DictDetailDO dictDetail) {
        DictDO dict = dictService.getById(dictDetail.getDictId());
        dicDetailCache.remove(dict.getName());
    }
}
