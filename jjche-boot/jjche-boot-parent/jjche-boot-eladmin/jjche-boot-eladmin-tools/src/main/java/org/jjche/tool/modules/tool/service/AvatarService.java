package org.jjche.tool.modules.tool.service;

import lombok.RequiredArgsConstructor;
import org.jjche.core.util.FileUtil;
import org.jjche.tool.modules.tool.config.FileProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * <p>
 * 头像
 * </p>
 *
 * @author miaoyj
 * @version 1.0.8-SNAPSHOT
 * @since 2020-11-02
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
