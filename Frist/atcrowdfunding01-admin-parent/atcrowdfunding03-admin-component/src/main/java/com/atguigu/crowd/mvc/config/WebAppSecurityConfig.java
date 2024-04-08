package com.atguigu.crowd.mvc.config;

import com.atguigu.crowd.constant.CrowdConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//表示当前类是配置类
@Configuration

//启用web环境下权限配置功能
@EnableWebSecurity
//部长生效
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class WebAppSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

//    @Bean
//    public BCryptPasswordEncoder getPasswordEncoder(){
//        return new BCryptPasswordEncoder();
//    }

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {

       // builder.inMemoryAuthentication().withUser("tom").password("123123").roles("ADMIN");

        builder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);



    }

    @Override
    protected void configure(HttpSecurity security) throws Exception {

        security.authorizeRequests()
                .antMatchers("/admin/to/login/page.html")
                .permitAll()
                .antMatchers("/bootstrap/**")
                .permitAll()
                .antMatchers("/css/**")
                .permitAll()
                .antMatchers("/fonts/**")
                .permitAll()
                .antMatchers("/img/**")
                .permitAll()
                .antMatchers("/jquery/**")
                .permitAll()
                .antMatchers("/layer/**")
                .permitAll()
                .antMatchers("/script/**")
                .permitAll()
                .antMatchers("/ztree/**")
                .permitAll()
                .antMatchers("/admin/get/page.html")
                //.hasRole("经理")
                .access("hasRole('经理') or hasAnyAuthority('user:get')")
                .anyRequest()
                .authenticated()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(new AccessDeniedHandler() {

                    @Override
                    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
                        request.setAttribute("exception",new Exception(CrowdConstant.MESSAGE_ACCESS_DENIED));
                        request.getRequestDispatcher("/WEB-INF/system-error.jsp").forward(request,response);
                    }
                })
                .and()
                .csrf()
                .disable() // 禁用 CSRF 功能
                .formLogin()
                .loginPage("/admin/to/login/page.html")
                .loginProcessingUrl("/security/do/login.html")
                .defaultSuccessUrl("/admin/to/main/page.html")
                .usernameParameter("loginAcct")
                .passwordParameter("userPswd")
                .and()
                .logout()
                .logoutUrl("/security/do/logout.html")
                .logoutSuccessUrl("/admin/to/login/page.html");
//                .and()
//                .csrf()
//                .disable(); // 禁用 CSRF 功能
////禁用 CSRF 功能。注意：这仅仅是我们学习过程中偷懒的做法，实际开发时还是不要禁用。
//        security.csrf().disable();

    }
}