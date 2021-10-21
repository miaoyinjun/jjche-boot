package com.boot.admin.sba.conf;

import com.boot.admin.common.constant.EnvConstant;
import de.codecentric.boot.admin.server.config.AdminServerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

/**
 * <p>
 * sba安全配置
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-07-09
 */
@Configuration
public class SbaSecurityConfig {

    /**
     * <p>
     * dev 环境加载
     * </p>
     *
     * @author miaoyj
     * @since 2020-08-25
     */

    @Profile(EnvConstant.DEV)
    @Configuration
    @Order(1)
    public static class SecurityPermitAllConfig extends WebSecurityConfigurerAdapter {
        private final String adminContextPath;

        public SecurityPermitAllConfig(AdminServerProperties adminServerProperties) {
            this.adminContextPath = adminServerProperties.getContextPath();
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable()
                    .headers().frameOptions().disable();
            /**解决与其它安全配置冲突问题*/
            http.antMatcher(adminContextPath + "/**").authorizeRequests()
                    .anyRequest()
                    .permitAll();
        }
    }

    /**
     * <p>
     * qa,demo,prod 环境加载
     * </p>
     *
     * @author miaoyj
     * @since 2020-08-25
     */
    @Profile({EnvConstant.QA, EnvConstant.DEMO, EnvConstant.PROD})
    @Configuration
    @Order(1)
    public static class SecuritySecureConfig extends WebSecurityConfigurerAdapter {
        @Autowired
        private SecurityProperties securityProperties;

        private final String adminContextPath;

        public SecuritySecureConfig(AdminServerProperties adminServerProperties) {
            this.adminContextPath = adminServerProperties.getContextPath();
        }

        @Bean
        @ConditionalOnMissingBean(PasswordEncoder.class)
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            PasswordEncoder encoder = new BCryptPasswordEncoder();

            SecurityProperties.User user = securityProperties.getUser();
            String username = user.getName();
            String password = user.getPassword();
            password = encoder.encode(password);
            auth.inMemoryAuthentication()
                    .withUser(username)
                    .password(password)
                    .roles("ADMIN");
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
            //登录跳转
            successHandler.setTargetUrlParameter("redirectTo");
//            successHandler.setDefaultTargetUrl(adminContextPath);
//            successHandler.setAlwaysUseDefaultTargetUrl(true);

            http.authorizeRequests()
                    .antMatchers(adminContextPath + "/assets/**").permitAll()
                    .antMatchers(adminContextPath + "/login").permitAll()
//					.anyRequest().authenticated()
//                    .antMatchers(adminContextPath + "/**").authenticated()
                    .and()
                    .formLogin().loginPage(adminContextPath + "/login").successHandler(successHandler).and()
                    .logout().logoutUrl(adminContextPath + "/logout").and()
                    .httpBasic().and()
                    .rememberMe().and()
                    .csrf().disable()
                    .headers().frameOptions().disable();
            /**解决与其它安全配置冲突问题*/
            http.antMatcher(adminContextPath + "/**").authorizeRequests()
                    .anyRequest().authenticated();
        }
    }

    /**
     * 解决 httpTrace失效
     *
     * @return a {@link org.springframework.boot.actuate.trace.http.HttpTraceRepository} object.

    //    @ConditionalOnMissingBean
    //    @Bean
    //    public HttpTraceRepository httpTraceRepository() {
    //        return new InMemoryHttpTraceRepository();
    //    }
     */
}
