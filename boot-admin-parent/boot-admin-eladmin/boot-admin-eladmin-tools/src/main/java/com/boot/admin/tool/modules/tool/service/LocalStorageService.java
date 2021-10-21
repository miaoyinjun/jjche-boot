package com.boot.admin.tool.modules.tool.service;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.boot.admin.common.util.ValidationUtil;
import com.boot.admin.core.util.FileUtil;
import com.boot.admin.mybatis.base.service.MyServiceImpl;
import com.boot.admin.mybatis.param.MyPage;
import com.boot.admin.mybatis.param.PageParam;
import com.boot.admin.mybatis.util.MybatisUtil;
import com.boot.admin.tool.modules.tool.config.FileProperties;
import com.boot.admin.tool.modules.tool.domain.LocalStorageDO;
import com.boot.admin.tool.modules.tool.dto.LocalStorageDTO;
import com.boot.admin.tool.modules.tool.dto.LocalStorageQueryCriteriaDTO;
import com.boot.admin.tool.modules.tool.mapper.LocalStorageMapper;
import com.boot.admin.tool.modules.tool.mapstruct.LocalStorageMapStruct;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>LocalStorageService class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2019-09-05
 */
@Service
@RequiredArgsConstructor
public class LocalStorageService extends MyServiceImpl<LocalStorageMapper, LocalStorageDO> {

    private final LocalStorageMapStruct localStorageMapper;
    private final FileProperties properties;


    /**
     * <p>
     * 获取列表查询语句
     * </p>
     *
     * @param criteria 条件
     * @return sql
     */
    private QueryWrapper queryWrapper(LocalStorageQueryCriteriaDTO criteria) {
        QueryWrapper queryWrapper = MybatisUtil.assemblyQueryWrapper(criteria);
        String blurry = criteria.getBlurry();
        if (StrUtil.isNotBlank(blurry)) {
            queryWrapper.apply("name LIKE {0} OR suffix LIKE {0} OR type LIKE {0}", "%" + blurry + "%");
        }
        return queryWrapper;
    }

    /**
     * 分页查询
     *
     * @param criteria 条件
     * @param pageable 分页参数
     * @return /
     */
    public MyPage queryAll(LocalStorageQueryCriteriaDTO criteria, PageParam pageable) {
        QueryWrapper queryWrapper = queryWrapper(criteria);
        MyPage myPage = this.page(pageable, queryWrapper);
        List<LocalStorageDTO> list = localStorageMapper.toVO(myPage.getRecords());
        myPage.setNewRecords(list);
        return myPage;
    }

    /**
     * 查询全部数据
     *
     * @param criteria 条件
     * @return /
     */
    public List<LocalStorageDTO> queryAll(LocalStorageQueryCriteriaDTO criteria) {
        QueryWrapper queryWrapper = queryWrapper(criteria);
        return localStorageMapper.toVO(this.list(queryWrapper));
    }

    /**
     * 根据ID查询
     *
     * @param id /
     * @return /
     */
    public LocalStorageDTO findById(Long id) {
        LocalStorageDO localStorage = this.getById(id);
        ValidationUtil.isNull(localStorage.getId(), "LocalStorageDO", "id", id);
        return localStorageMapper.toVO(localStorage);
    }

    /**
     * 上传
     *
     * @param name 文件名称
     * @param multipartFile 文件
     * @return a {@link com.boot.admin.tool.modules.tool.domain.LocalStorageDO} object.
     */
    @Transactional(rollbackFor = Exception.class)
    public LocalStorageDO create(String name, MultipartFile multipartFile) {
        FileUtil.checkSize(properties.getMaxSize(), multipartFile.getSize());
        String suffix = FileUtil.getExtensionName(multipartFile.getOriginalFilename());
        String type = FileUtil.getFileType(suffix);
        File file = FileUtil.upload(multipartFile, properties.getPath().getPath() + type + File.separator);
        Assert.notNull(file, "上传失败");
        try {
            name = StringUtils.isBlank(name) ? FileUtil.getFileNameNoEx(multipartFile.getOriginalFilename()) : name;
            LocalStorageDO localStorage = new LocalStorageDO(
                    file.getName(),
                    name,
                    suffix,
                    file.getPath(),
                    type,
                    FileUtil.getSize(multipartFile.getSize())
            );
            this.save(localStorage);
            return localStorage;
        } catch (Exception e) {
            FileUtil.del(file);
            throw e;
        }
    }

    /**
     * 编辑
     *
     * @param resources 文件信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(LocalStorageDO resources) {
        this.updateById(resources);
    }

    /**
     * 多选删除
     *
     * @param ids /
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteAll(List<Long> ids) {
        for (Long id : ids) {
            LocalStorageDO storage = this.getById(id);
            FileUtil.del(storage.getPath());
        }
        this.removeByIds(ids);
    }

    /**
     * 导出数据
     *
     * @param queryAll 待导出的数据
     * @param response /
     * @throws java.io.IOException if any.
     */
    public void download(List<LocalStorageDTO> queryAll, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (LocalStorageDTO localStorageDTO : queryAll) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("文件名", localStorageDTO.getRealName());
            map.put("备注名", localStorageDTO.getName());
            map.put("文件类型", localStorageDTO.getType());
            map.put("文件大小", localStorageDTO.getSize());
            map.put("创建者", localStorageDTO.getCreatedBy());
            map.put("创建日期", localStorageDTO.getGmtCreate());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
