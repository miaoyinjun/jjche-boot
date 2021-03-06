package aorg.jjche.sba.conf;

import cn.hutool.core.util.BooleanUtil;
import de.codecentric.boot.admin.server.config.AdminServerProperties;
import org.jjche.core.util.SpringContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
     * qa,demo,prod 环境加载
     * cloud版本全部放开
     * </p>
     */
    @Configuration
    @Order(1)
    public static class SecuritySecureConfig extends WebSecurityConfigurerAdapter {
        private final String adminContextPath;
        @Autowired
        private SecurityProperties securityProperties;

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
            boolean isNotDev = !SpringContextHolder.isDev();
            //单体版才支持登录
            if (BooleanUtil.isFalse(SpringContextHolder.isCloud()) && isNotDev) {
                PasswordEncoder encoder = new BCryptPasswordEncoder();
                SecurityProperties.User user = securityProperties.getUser();
                String username = user.getName();
                String password = user.getPassword();
                password = encoder.encode(password);
                auth.inMemoryAuthentication().withUser(username).password(password).roles("ADMIN");
            }
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            //单体版才支持登录
            if (SpringContextHolder.isCloud() || SpringContextHolder.isDev()) {
                http.csrf().disable().headers().frameOptions().disable();
                /**解决与其它安全配置冲突问题*/
                http.antMatcher(adminContextPath + "/**").authorizeRequests().anyRequest().permitAll();
            } else {
                SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
                //登录跳转
                successHandler.setTargetUrlParameter("redirectTo");

                http.authorizeRequests().antMatchers(adminContextPath + "/assets/**").permitAll().antMatchers(adminContextPath + "/login").permitAll()
//					.anyRequest().authenticated()
//                    .antMatchers(adminContextPath + "/**").authenticated()
                        .and().formLogin().loginPage(adminContextPath + "/login").successHandler(successHandler).and().logout().logoutUrl(adminContextPath + "/logout").and().httpBasic().and().rememberMe().and().csrf().disable().headers().frameOptions().disable();
                /**解决与其它安全配置冲突问题*/
                http.antMatcher(adminContextPath + "/**").authorizeRequests().anyRequest().authenticated();
            }
        }
    }

/**
 * 解决 httpTrace失效
 *
 * @return a {@link org.springframework.boot.actuate.trace.http.HttpTraceRepository} object.
 * <p>
 * //    @ConditionalOnMissingBean
 * //    @Bean
 * //    public HttpTraceRepository httpTraceRepository() {
 * //        return new InMemoryHttpTraceRepository();
 * //    }
 */
}