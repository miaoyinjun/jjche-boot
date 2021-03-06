package org.jjche.tool.modules.tool.service;

import cn.hutool.core.lang.Assert;
import com.alibaba.fastjson.JSON;
import com.alicp.jetcache.anno.CacheInvalidate;
import com.alicp.jetcache.anno.Cached;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import lombok.RequiredArgsConstructor;
import org.jjche.common.constant.CacheKey;
import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.common.util.ValidationUtil;
import org.jjche.core.util.FileUtil;
import org.jjche.mybatis.base.service.MyServiceImpl;
import org.jjche.mybatis.param.SortEnum;
import org.jjche.mybatis.util.MybatisUtil;
import org.jjche.tool.modules.tool.domain.QiniuConfigDO;
import org.jjche.tool.modules.tool.domain.QiniuContentDO;
import org.jjche.tool.modules.tool.dto.QiniuQueryCriteriaDTO;
import org.jjche.tool.modules.tool.mapper.QiNiuConfigMapper;
import org.jjche.tool.modules.tool.mapper.QiniuContentMapper;
import org.jjche.tool.modules.tool.utils.QiNiuUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>QiNiuService class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2018-12-31
 */
@Service
@RequiredArgsConstructor
public class QiNiuService extends MyServiceImpl<QiNiuConfigMapper, QiniuConfigDO> {

    private final QiniuContentMapper qiniuContentRepository;

    @Value("${jjche.tool.qiniu.max-size}")
    private Long maxSize;

    /**
     * ?????????
     *
     * @return QiniuConfigDO
     */
    @Cached(name = CacheKey.QI_NIU)
    public QiniuConfigDO find() {
        return this.getById(1L);
    }

    /**
     * ????????????
     *
     * @param qiniuConfig ??????
     * @return QiniuConfigDO
     */
    @CacheInvalidate(name = CacheKey.QI_NIU)
    @Transactional(rollbackFor = Exception.class)
    public QiniuConfigDO config(QiniuConfigDO qiniuConfig) {
        qiniuConfig.setId(1L);
        String http = "http://", https = "https://";
        Boolean isHttpOrHttps = qiniuConfig.getHost().toLowerCase().startsWith(http) || qiniuConfig.getHost().toLowerCase().startsWith(https);
        Assert.isTrue(isHttpOrHttps, "?????????????????????http://??????https://??????");
        this.saveOrUpdate(qiniuConfig);
        return qiniuConfig;
    }


    /**
     * <p>
     * ????????????????????????
     * </p>
     *
     * @param criteria ??????
     * @return sql
     */
    private LambdaQueryWrapper queryWrapper(QiniuQueryCriteriaDTO criteria) {
        return MybatisUtil.assemblyLambdaQueryWrapper(criteria, SortEnum.ID_DESC);
    }

    /**
     * ????????????
     *
     * @param criteria ??????
     * @param pageable ????????????
     * @return /
     */
    public MyPage queryAll(QiniuQueryCriteriaDTO criteria, PageParam pageable) {
        LambdaQueryWrapper queryWrapper = queryWrapper(criteria);
        return this.page(pageable, queryWrapper);
    }

    /**
     * ????????????
     *
     * @param criteria ??????
     * @return /
     */
    public List<QiniuContentDO> queryAll(QiniuQueryCriteriaDTO criteria) {
        LambdaQueryWrapper queryWrapper = queryWrapper(criteria);
        return this.list(queryWrapper);
    }


    /**
     * <p>
     * ?????????????????????
     * </p>
     *
     * @param name ?????????
     * @return /
     */
    private QiniuContentDO findByName(String name) {
        LambdaQueryWrapper<QiniuContentDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(QiniuContentDO::getName, name);
        return qiniuContentRepository.selectOne(queryWrapper);
    }

    /**
     * ????????????
     *
     * @param file        ??????
     * @param qiniuConfig ??????
     * @return QiniuContentDO
     */
    @Transactional(rollbackFor = Exception.class)
    public QiniuContentDO upload(MultipartFile file, QiniuConfigDO qiniuConfig) {
        FileUtil.checkSize(maxSize, file.getSize());
        Assert.notNull(qiniuConfig, "????????????????????????????????????");
        // ?????????????????????Zone??????????????????
        Configuration cfg = new Configuration(QiNiuUtil.getRegion(qiniuConfig.getZone()));
        UploadManager uploadManager = new UploadManager(cfg);
        Auth auth = Auth.create(qiniuConfig.getAccessKey(), qiniuConfig.getSecretKey());
        String upToken = auth.uploadToken(qiniuConfig.getBucket());
        try {
            String key = file.getOriginalFilename();
            QiniuContentDO content = this.findByName(key);
            if (content != null) {
                key = QiNiuUtil.getKey(key);
            }
            Response response = uploadManager.put(file.getBytes(), key, upToken);
            //???????????????????????????

            DefaultPutRet putRet = JSON.parseObject(response.bodyString(), DefaultPutRet.class);
            content = this.findByName(FileUtil.getFileNameNoEx(putRet.key));
            if (content == null) {
                //???????????????
                QiniuContentDO qiniuContent = new QiniuContentDO();
                qiniuContent.setSuffix(FileUtil.getExtensionName(putRet.key));
                qiniuContent.setBucket(qiniuConfig.getBucket());
                qiniuContent.setType(qiniuConfig.getType());
                qiniuContent.setName(FileUtil.getFileNameNoEx(putRet.key));
                qiniuContent.setUrl(qiniuConfig.getHost() + "/" + putRet.key);
                qiniuContent.setSize(FileUtil.getSize(Integer.parseInt(file.getSize() + "")));
                qiniuContentRepository.insert(qiniuContent);
                return qiniuContent;
            }
            return content;
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    /**
     * ????????????
     *
     * @param id ??????ID
     * @return QiniuContentDO
     */
    public QiniuContentDO findByContentId(Long id) {
        QiniuContentDO qiniuContent = qiniuContentRepository.selectById(id);
        ValidationUtil.isNull(qiniuContent.getId(), "QiniuContentDO", "id", id);
        return qiniuContent;
    }

    /**
     * ????????????
     *
     * @param content ????????????
     * @param config  ??????
     * @return String
     */
    public String download(QiniuContentDO content, QiniuConfigDO config) {
        String finalUrl;
        String type = "??????";
        if (type.equals(content.getType())) {
            finalUrl = content.getUrl();
        } else {
            Auth auth = Auth.create(config.getAccessKey(), config.getSecretKey());
            // 1??????????????????????????????????????????
            long expireInSeconds = 3600;
            finalUrl = auth.privateDownloadUrl(content.getUrl(), expireInSeconds);
        }
        return finalUrl;
    }

    /**
     * ????????????
     *
     * @param content ??????
     * @param config  ??????
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(QiniuContentDO content, QiniuConfigDO config) {
        //?????????????????????Zone??????????????????
        Configuration cfg = new Configuration(QiNiuUtil.getRegion(config.getZone()));
        Auth auth = Auth.create(config.getAccessKey(), config.getSecretKey());
        BucketManager bucketManager = new BucketManager(auth, cfg);
        try {
            bucketManager.delete(content.getBucket(), content.getName() + "." + content.getSuffix());
        } catch (QiniuException e) {
            e.printStackTrace();
        }
        qiniuContentRepository.deleteById(content.getId());
    }

    /**
     * ????????????
     *
     * @param config ??????
     */
    @Transactional(rollbackFor = Exception.class)
    public void synchronize(QiniuConfigDO config) {
        Assert.notNull(config.getId(), "????????????????????????????????????");
        //?????????????????????Zone??????????????????
        Configuration cfg = new Configuration(QiNiuUtil.getRegion(config.getZone()));
        Auth auth = Auth.create(config.getAccessKey(), config.getSecretKey());
        BucketManager bucketManager = new BucketManager(auth, cfg);
        //???????????????
        String prefix = "";
        //????????????????????????????????????1000???????????? 1000
        int limit = 1000;
        //?????????????????????????????????????????????????????????????????????????????????????????????????????????
        String delimiter = "";
        //????????????????????????
        BucketManager.FileListIterator fileListIterator = bucketManager.createFileListIterator(config.getBucket(), prefix, limit, delimiter);
        while (fileListIterator.hasNext()) {
            //???????????????file list??????
            QiniuContentDO qiniuContent;
            FileInfo[] items = fileListIterator.next();
            for (FileInfo item : items) {
                if (this.findByName(FileUtil.getFileNameNoEx(item.key)) == null) {
                    qiniuContent = new QiniuContentDO();
                    qiniuContent.setSize(FileUtil.getSize(Integer.parseInt(item.fsize + "")));
                    qiniuContent.setSuffix(FileUtil.getExtensionName(item.key));
                    qiniuContent.setName(FileUtil.getFileNameNoEx(item.key));
                    qiniuContent.setType(config.getType());
                    qiniuContent.setBucket(config.getBucket());
                    qiniuContent.setUrl(config.getHost() + "/" + item.key);
                    qiniuContentRepository.insert(qiniuContent);
                }
            }
        }
    }

    /**
     * ????????????
     *
     * @param ids    ??????ID??????
     * @param config ??????
     */
    public void deleteAll(Long[] ids, QiniuConfigDO config) {
        for (Long id : ids) {
            delete(findByContentId(id), config);
        }
    }

    /**
     * ????????????
     *
     * @param type ??????
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(String type) {
        UpdateWrapper updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("type", type);
        this.update(updateWrapper);
    }

    /**
     * ????????????
     *
     * @param queryAll /
     * @param response /
     * @throws java.io.IOException if any.
     */
    public void downloadList(List<QiniuContentDO> queryAll, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (QiniuContentDO content : queryAll) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("?????????", content.getName());
            map.put("????????????", content.getSuffix());
            map.put("????????????", content.getBucket());
            map.put("????????????", content.getSize());
            map.put("????????????", content.getType());
            map.put("????????????", content.getGmtModified());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
