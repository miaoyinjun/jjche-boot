package org.jjche.core.url;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.BooleanUtil;
import org.jjche.core.annotation.controller.ApiRestController;
import org.jjche.core.annotation.controller.SysRestController;
import org.jjche.core.property.CoreApiPathProperties;
import org.jjche.core.property.CoreProperties;
import org.jjche.core.util.SpringContextHolder;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.Resource;
import java.lang.reflect.Method;

/**
 * <p>
 * Url前缀
 * </p>
 *
 * @author miaoyj
 * @since 2022-04-29
 */
public class AutoPrefixUrlMapping extends RequestMappingHandlerMapping {
    @Resource
    private CoreProperties coreProperties;

    @Override
    protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
        CoreApiPathProperties coreApiPathProperties = coreProperties.getApi().getPath();
        //获取url前缀
        String prefix = coreApiPathProperties.getGlobalPrefix();
        RequestMappingInfo requestMappingInfo = super.getMappingForMethod(method, handlerType);
        boolean apiRest = AnnotationUtil.hasAnnotation(handlerType, ApiRestController.class);
        boolean sysRest = AnnotationUtil.hasAnnotation(handlerType, SysRestController.class);
        if ((apiRest || sysRest) && null != requestMappingInfo) {
            //单体
            if (BooleanUtil.isFalse(SpringContextHolder.isCloud())) {
                if (sysRest) {
                    prefix += coreApiPathProperties.getSysPrefix();
                } else {
                    prefix += coreApiPathProperties.getPrefix();
                }
            }
            //根据url前缀生成RequestMappingInfo并与原有的RequestMappingInfo合并
            requestMappingInfo = RequestMappingInfo.paths(prefix).build().combine(requestMappingInfo);
        }
        return requestMappingInfo;
    }
}
