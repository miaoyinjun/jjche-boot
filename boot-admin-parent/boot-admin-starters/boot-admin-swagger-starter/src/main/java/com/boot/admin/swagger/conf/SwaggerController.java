package com.boot.admin.swagger.conf;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <p>
 * 重新定义地址
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-07-09
 */
@Controller
@RequestMapping("/sba/api")
public class SwaggerController {
    /**
     * <p>resource</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @GetMapping("/swagger-resources")
    public String resource() {
        return "forward:/swagger-resources";
    }

    /**
     * <p>ui</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @GetMapping("/swagger-resources/configuration/ui")
    public String ui() {
        return "forward:/swagger-resources/configuration/ui";
    }

    /**
     * <p>doc</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @GetMapping("/v2/api-docs")
    public String doc() {
        return "forward:/v2/api-docs";
    }

    /**
     * <p>docExt</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @GetMapping("/v2/api-docs-ext")
    public String docExt() {
        return "forward:/v2/api-docs-ext";
    }

    /**
     * <p>security</p>
     *
     * @return a {@link java.lang.String} object.
     */
    @GetMapping("/swagger-resources/configuration/security")
    public String ssecurity() {
        return "forward:/swagger-resources/configuration/security";
    }
}
