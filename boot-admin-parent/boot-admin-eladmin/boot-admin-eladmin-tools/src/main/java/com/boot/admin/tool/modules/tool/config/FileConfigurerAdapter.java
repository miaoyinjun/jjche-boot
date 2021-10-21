package com.boot.admin.tool.modules.tool.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * WebMvcConfigurer
 *
 * @author Zheng Jie
 * @since 2018-11-30
 * @version 1.0.8-SNAPSHOT
 */
@Configuration
@EnableWebMvc
public class FileConfigurerAdapter implements WebMvcConfigurer {

    /**
     * 文件配置
     */
    @Autowired
    private FileProperties properties;

    /**
     * <p>Constructor for FileConfigurerAdapter.</p>
     *
     * @param properties a {@link com.boot.admin.tool.modules.tool.config.FileProperties} object.
     */
    public FileConfigurerAdapter(FileProperties properties) {
        this.properties = properties;
    }

    /** {@inheritDoc} */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        FileProperties.ElPath path = properties.getPath();
        String avatarUtl = "file:" + path.getAvatar().replace("\\", "/");
        String pathUtl = "file:" + path.getPath().replace("\\", "/");
        registry.addResourceHandler("/avatar/**").addResourceLocations(avatarUtl).setCachePeriod(0);
        registry.addResourceHandler("/file/**").addResourceLocations(pathUtl).setCachePeriod(0);
    }
}
