package com.boot.admin.tool.modules.tool.config;

import com.boot.admin.common.constant.ElAdminConstant;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * <p>FileProperties class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "boot.admin.tool.file")
public class FileProperties {

    /** 文件大小限制 */
    private Long maxSize;

    /** 头像大小限制 */
    private Long avatarMaxSize;

    private ElPath mac;

    private ElPath linux;

    private ElPath windows;

    /**
     * <p>getPath.</p>
     *
     * @return a {@link com.boot.admin.tool.modules.tool.config.FileProperties.ElPath} object.
     */
    public ElPath getPath(){
        String os = System.getProperty("os.name");
        if(os.toLowerCase().startsWith(ElAdminConstant.WIN)) {
            return windows;
        } else if(os.toLowerCase().startsWith(ElAdminConstant.MAC)){
            return mac;
        }
        return linux;
    }

    @Data
    public static class ElPath{

        private String path;

        private String avatar;
    }
}
