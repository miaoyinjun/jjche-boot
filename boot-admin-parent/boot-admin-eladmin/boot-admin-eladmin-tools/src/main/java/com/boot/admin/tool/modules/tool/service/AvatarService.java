package com.boot.admin.tool.modules.tool.service;

import com.boot.admin.core.util.FileUtil;
import com.boot.admin.tool.modules.tool.config.FileProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * <p>
 * 头像
 * </p>
 *
 * @author miaoyj
 * @since 2020-11-02
 * @version 1.0.8-SNAPSHOT
 */
@Service
@RequiredArgsConstructor
public class AvatarService {
    private final FileProperties properties;

    /**
     * <p>
     * 上传
     * </p>
     *
     * @param multipartFile 文件
     * @return /
     */
    public File uploadAvatar(MultipartFile multipartFile) {
        return FileUtil.upload(multipartFile, properties.getPath().getAvatar());
    }
}
