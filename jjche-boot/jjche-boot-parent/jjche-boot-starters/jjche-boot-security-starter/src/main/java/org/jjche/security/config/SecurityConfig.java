package org.jjche.security.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.jjche.common.api.CommonAPI;
import org.jjche.common.constant.SecurityConstant;
import org.jjche.core.util.SpringContextHolder;
import org.jjche.security.annotation.AnonymousAccess;
import org.jjche.security.auth.sms.SmsCodeAuthenticationProvider;
import org.jjche.security.handler.JwtAuthenticationAccessDeniedHandler;
import org.jjche.security.handler.JwtAuthenticationEntryPoint;
import org.jjche.security.property.SecurityProperties;
import org.jjche.security.property.SecurityRoleUrlProperties;
import org.jjche.security.property.SecurityUrlProperties;
import org.jjche.security.security.TokenConfigurer;
import org.jjche.security.util.RequestMethodEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.*;

/**
 * <p>SecurityConfig class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Order(2)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private static String ROLE_NAME_PREFIX = "ROLE_";

    private final JwtAuthenticationEntryPoint authenticationErrorHandler;
    private final JwtAuthenticationAccessDeniedHandler jwtAccessDeniedHandler;
    private final ApplicationContext applicationContext;
    private final SecurityProperties properties;
    private final CommonAPI commonAPI;
    private final UserDetailsService userDetailsService;
    private final UserDetailsService smsUserDetailsService;

    @Bean
    GrantedAuthorityDefaults grantedAuthorityDefaults() {
        // ?????? ROLE_ ??????
        return new GrantedAuthorityDefaults("");
    }

    /**
     * <p>passwordEncoder.</p>
     *
     * @return a {@link org.springframework.security.crypto.password.PasswordEncoder} object.
     */
    @Bean
    @ConditionalOnMissingBean(PasswordEncoder.class)
    public PasswordEncoder passwordEncoder() {
        // ??????????????????
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) {
        SecurityUrlProperties securityUrlProperties = properties.getUrl();
        // ?????????????????? url??? @AnonymousAccess
        Map<RequestMappingInfo, HandlerMethod> handlerMethodMap = applicationContext.getBean(RequestMappingHandlerMapping.class).getHandlerMethods();
        // ????????????????????????????????????????????????????????????url
        Map<String, Set<String>> anonymousUrls = getAnonymousUrl(handlerMethodMap, securityUrlProperties);
        web.ignoring()
                // ??????OPTIONS??????
                .antMatchers(HttpMethod.OPTIONS, "/**")
                // ???????????????????????????url???????????????????????????Token??????????????????????????? Request ??????
                // GET
                .antMatchers(HttpMethod.GET, anonymousUrls.get(RequestMethodEnum.GET.getType()).toArray(new String[0]))
                // POST
                .antMatchers(HttpMethod.POST, anonymousUrls.get(RequestMethodEnum.POST.getType()).toArray(new String[0]))
                // PUT
                .antMatchers(HttpMethod.PUT, anonymousUrls.get(RequestMethodEnum.PUT.getType()).toArray(new String[0]))
                // PATCH
                .antMatchers(HttpMethod.PATCH, anonymousUrls.get(RequestMethodEnum.PATCH.getType()).toArray(new String[0]))
                // DELETE
                .antMatchers(HttpMethod.DELETE, anonymousUrls.get(RequestMethodEnum.DELETE.getType()).toArray(new String[0]))
                // ??????????????????????????????
                .antMatchers(anonymousUrls.get(RequestMethodEnum.ALL.getType()).toArray(new String[0]));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry authorizeRequests = httpSecurity.authorizeRequests();
        //???????????????????????????????????????
        SecurityUrlProperties securityUrlProperties = properties.getUrl();
        authorizeRequests = setPropertityUrl(authorizeRequests, securityUrlProperties);

        authorizeRequests.and()
                // ?????? CSRF
                .csrf().disable()
                // ????????????
                .exceptionHandling().authenticationEntryPoint(authenticationErrorHandler).accessDeniedHandler(jwtAccessDeniedHandler)
                // ??????iframe ????????????
                .and().headers().frameOptions().disable()
                // ???????????????
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests();

        // cloud?????????????????????????????????TokenFilter
        if (SpringContextHolder.isCloud()) {
            authorizeRequests.anyRequest().permitAll();
        }// ???????????????????????????
        else {
            authorizeRequests.anyRequest().authenticated().and().apply(securityConfigurerAdapter());
        }
    }

    /**
     * <p>configureGlobal.</p>
     *
     * @param auth a {@link org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder} object.
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(smsCodeAuthenticationProvider());
        auth.authenticationProvider(usernamePasswordAuthenticationProvider());
    }

    /**
     * <p>
     * ????????????
     * </p>
     *
     * @return ??????
     */
    @Bean("smsCodeAuthenticationProvider")
    SmsCodeAuthenticationProvider smsCodeAuthenticationProvider() {
        SmsCodeAuthenticationProvider smsCodeAuthenticationProvider = new SmsCodeAuthenticationProvider();
        smsCodeAuthenticationProvider.setUserDetailsService(smsUserDetailsService);
        return smsCodeAuthenticationProvider;
    }

    /**
     * <p>
     * ????????????
     * </p>
     *
     * @return ??????
     */
    @Bean("usernamePasswordAuthenticationProvider")
    DaoAuthenticationProvider usernamePasswordAuthenticationProvider() {
        DaoAuthenticationProvider usernamePasswordAuthenticationProvider = new DaoAuthenticationProvider();
        usernamePasswordAuthenticationProvider.setUserDetailsService(userDetailsService);
        if (passwordEncoder() != null) {
            usernamePasswordAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        }
        usernamePasswordAuthenticationProvider.setHideUserNotFoundExceptions(false);
        return usernamePasswordAuthenticationProvider;
    }

    private ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry setPropertityUrl(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry authorizeRequests, SecurityUrlProperties securityUrlProperties) {
        if (ObjectUtil.isNotNull(securityUrlProperties)) {
            List<SecurityRoleUrlProperties> roleUrlList = securityUrlProperties.getRoleUrls();
            if (CollUtil.isNotEmpty(roleUrlList)) {
                for (SecurityRoleUrlProperties roleUrl : roleUrlList) {
                    String roleName = roleUrl.getRoleName();
                    List<String> rUrlList = roleUrl.getUrls();
                    if (StrUtil.isNotBlank(roleName) && CollUtil.isNotEmpty(rUrlList)) {
                        /** hasAnyRole ??????ROLE_??????*/
                        if (roleName.startsWith(ROLE_NAME_PREFIX)) {
                            roleName = StrUtil.removePrefix(roleName, ROLE_NAME_PREFIX);
                        }
                        for (String url : rUrlList) {
                            authorizeRequests.antMatchers(url).hasAnyRole(roleName);
                        }
                    }
                }
            }
        }
        return authorizeRequests;
    }

    private Map<String, Set<String>> getAnonymousUrl(Map<RequestMappingInfo, HandlerMethod> handlerMethodMap, SecurityUrlProperties securityUrlProperties) {
        Map<String, Set<String>> anonymousUrls = new HashMap<>(8);
        Set<String> get = new HashSet<>();
        Set<String> post = new HashSet<>();
        Set<String> put = new HashSet<>();
        Set<String> patch = new HashSet<>();
        Set<String> delete = new HashSet<>();
        Set<String> all = new HashSet<>();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> infoEntry : handlerMethodMap.entrySet()) {
            HandlerMethod handlerMethod = infoEntry.getValue();
            AnonymousAccess anonymousAccess = handlerMethod.getMethodAnnotation(AnonymousAccess.class);
            if (null != anonymousAccess) {
                List<RequestMethod> requestMethods = new ArrayList<>(infoEntry.getKey().getMethodsCondition().getMethods());
                RequestMethodEnum request = RequestMethodEnum.find(requestMethods.size() == 0 ? RequestMethodEnum.ALL.getType() : requestMethods.get(0).name());
                switch (Objects.requireNonNull(request)) {
                    case GET:
                        get.addAll(infoEntry.getKey().getPatternsCondition().getPatterns());
                        break;
                    case POST:
                        post.addAll(infoEntry.getKey().getPatternsCondition().getPatterns());
                        break;
                    case PUT:
                        put.addAll(infoEntry.getKey().getPatternsCondition().getPatterns());
                        break;
                    case PATCH:
                        patch.addAll(infoEntry.getKey().getPatternsCondition().getPatterns());
                        break;
                    case DELETE:
                        delete.addAll(infoEntry.getKey().getPatternsCondition().getPatterns());
                        break;
                    default:
                        all.addAll(infoEntry.getKey().getPatternsCondition().getPatterns());
                        break;
                }
            }

            /** ??????????????????URL*/
            List<String> excludeAllUrls = new ArrayList<>();
            List<String> excludeDefaultUrls = securityUrlProperties.getExcludeDefaultUrls();
            List<String> excludeUrls = securityUrlProperties.getExcludeUrls();
            if (CollUtil.isNotEmpty(excludeUrls)) {
                excludeAllUrls.addAll(excludeUrls);
            }
            if (CollUtil.isNotEmpty(excludeDefaultUrls)) {
                excludeAllUrls.addAll(excludeDefaultUrls);
                for (String excludeUrl : excludeAllUrls) {
                    get.add(excludeUrl);
                    post.add(excludeUrl);
                    put.add(excludeUrl);
                    patch.add(excludeUrl);
                    delete.add(excludeUrl);
                    all.add(excludeUrl);
                }
            }
        }
        anonymousUrls.put(RequestMethodEnum.GET.getType(), get);
        anonymousUrls.put(RequestMethodEnum.POST.getType(), post);
        anonymousUrls.put(RequestMethodEnum.PUT.getType(), put);
        anonymousUrls.put(RequestMethodEnum.PATCH.getType(), patch);
        anonymousUrls.put(RequestMethodEnum.DELETE.getType(), delete);
        anonymousUrls.put(RequestMethodEnum.ALL.getType(), all);

        //????????????url???????????????????????????swagger?????????
        SecurityConstant.ANONYMOUS_URLS.addAll(get);
        SecurityConstant.ANONYMOUS_URLS.addAll(post);
        SecurityConstant.ANONYMOUS_URLS.addAll(put);
        SecurityConstant.ANONYMOUS_URLS.addAll(patch);
        SecurityConstant.ANONYMOUS_URLS.addAll(delete);
        SecurityConstant.ANONYMOUS_URLS.addAll(all);
        return anonymousUrls;
    }

    private TokenConfigurer securityConfigurerAdapter() {
        return new TokenConfigurer(commonAPI);
    }


}
