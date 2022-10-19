package org.jjche.cat.integration.filter;

import com.dianping.cat.servlet.CatFilter;
import org.jjche.common.util.StrUtil;
import org.jjche.core.property.CoreApiPathProperties;
import org.jjche.core.property.CoreProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * <p>
 * cat的的核心过滤器
 * </p>
 *
 * @author miaoyj
 * @since 2022-10-13
 */
@Configuration
public class CatFilterConfigure {
    @Resource
    private CoreProperties coreProperties;

    @Bean
    public FilterRegistrationBean catFilter() {
        CoreApiPathProperties coreApiPathProperties = coreProperties.getApi().getPath();
        //获取url前缀
        String globalPrefix = coreApiPathProperties.getGlobalPrefix();
        String prefix = coreApiPathProperties.getPrefix();
        String urlPatterns = StrUtil.format("{}{}/*", globalPrefix, prefix);
        urlPatterns = StrUtil.replace(urlPatterns, "//", "/");
        FilterRegistrationBean registration = new FilterRegistrationBean();
        CatFilter catFilter = new CatFilter();
        registration.setFilter(catFilter);
        registration.addUrlPatterns(urlPatterns);
        registration.setOrder(1);
        return registration;
    }
}