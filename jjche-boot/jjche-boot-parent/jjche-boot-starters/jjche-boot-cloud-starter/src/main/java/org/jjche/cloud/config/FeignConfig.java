package org.jjche.cloud.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.log.StaticLog;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.alibaba.fastjson.support.springfox.SwaggerJsonSerializer;
import feign.Feign;
import feign.Logger;
import feign.RequestInterceptor;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.jjche.common.constant.SecurityConstant;
import org.jjche.security.property.SecurityJwtProperties;
import org.jjche.security.property.SecurityProperties;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

@ConditionalOnClass(Feign.class)
@AutoConfigureBefore(FeignAutoConfiguration.class)
@Configuration
public class FeignConfig {
    @Autowired
    private SecurityProperties properties;

    @Bean
    public RequestInterceptor requestInterceptor() {
        SecurityJwtProperties securityJwtProperties = properties.getJwt();
        String authHeader = securityJwtProperties.getHeader();
        return requestTemplate -> {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (null != attributes) {
                HttpServletRequest request = attributes.getRequest();
                StaticLog.debug("Feign request: {}", request.getRequestURI());
                // ???token????????????header???
                String token = request.getHeader(authHeader);
                if (token == null || "".equals(token)) {
                    token = request.getParameter("token");
                }
                StaticLog.debug("Feign request token: {}", token);
                requestTemplate.header(authHeader, token);
                // ??????????????????????????????????????????
                String userId = request.getHeader(SecurityConstant.JWT_KEY_USER_ID);
                String username = request.getHeader(SecurityConstant.JWT_KEY_USERNAME);
                Enumeration<String> elPermissions = request.getHeaders(SecurityConstant.JWT_KEY_PERMISSION);
                Enumeration<String> dataScopeDeptIds = request.getHeaders(SecurityConstant.JWT_KEY_DATA_SCOPE_DEPT_IDS);
                String dataScopeIsAll = request.getHeader(SecurityConstant.JWT_KEY_DATA_SCOPE_IS_ALL);
                String dataScopeIsSelf = request.getHeader(SecurityConstant.JWT_KEY_DATA_SCOPE_IS_SELF);
                String dataScopeUserid = request.getHeader(SecurityConstant.JWT_KEY_DATA_SCOPE_USERID);
                String dataScopeUsername = request.getHeader(SecurityConstant.JWT_KEY_DATA_SCOPE_USERNAME);

                requestTemplate.header(SecurityConstant.JWT_KEY_USER_ID, userId);
                requestTemplate.header(SecurityConstant.JWT_KEY_USERNAME, username);
                requestTemplate.header(SecurityConstant.JWT_KEY_PERMISSION, CollUtil.newArrayList(elPermissions));
                //????????????
                requestTemplate.header(SecurityConstant.JWT_KEY_DATA_SCOPE_DEPT_IDS, CollUtil.newArrayList(dataScopeDeptIds));
                requestTemplate.header(SecurityConstant.JWT_KEY_DATA_SCOPE_IS_ALL, dataScopeIsAll);
                requestTemplate.header(SecurityConstant.JWT_KEY_DATA_SCOPE_IS_SELF, dataScopeIsSelf);
                requestTemplate.header(SecurityConstant.JWT_KEY_DATA_SCOPE_USERID, dataScopeUserid);
                requestTemplate.header(SecurityConstant.JWT_KEY_DATA_SCOPE_USERNAME, dataScopeUsername);
            }
        };
    }

    /**
     * Feign ??????????????????????????????????????????NONE
     * Logger.Level ????????????????????????
     * NONE????????????????????????
     * BASIC???????????????????????????URL????????????????????????????????????
     * HEADERS??????????????? BASIC????????????????????????????????????????????????????????????
     * FULL?????????????????????????????????????????????????????????????????????????????????
     */
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    /**
     * Feign??????????????????
     *
     * @param messageConverters
     * @return
     */
    @Bean
    @Primary
    @Scope("prototype")
    public Encoder multipartFormEncoder(ObjectFactory<HttpMessageConverters> messageConverters) {
        return new SpringFormEncoder(new SpringEncoder(messageConverters));
    }

    //    @Bean
//    public Encoder feignEncoder() {
//        return new SpringEncoder(feignHttpMessageConverter());
//    }
//
//    @Bean
    public Decoder feignDecoder() {
        return new SpringDecoder(feignHttpMessageConverter());
    }

    /**
     * ??????????????????fastjson
     *
     * @return
     */
    private ObjectFactory<HttpMessageConverters> feignHttpMessageConverter() {
        final HttpMessageConverters httpMessageConverters = new HttpMessageConverters(this.getFastJsonConverter());
        return () -> httpMessageConverters;
    }

    private FastJsonHttpMessageConverter getFastJsonConverter() {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();

        List<MediaType> supportedMediaTypes = new ArrayList<>();
        MediaType mediaTypeJson = MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE);
        supportedMediaTypes.add(mediaTypeJson);
        converter.setSupportedMediaTypes(supportedMediaTypes);
        FastJsonConfig config = new FastJsonConfig();
        config.getSerializeConfig().put(JSON.class, new SwaggerJsonSerializer());
        config.setSerializerFeatures(SerializerFeature.DisableCircularReferenceDetect);
        converter.setFastJsonConfig(config);

        return converter;
    }
}