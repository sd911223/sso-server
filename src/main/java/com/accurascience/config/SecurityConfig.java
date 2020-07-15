package com.accurascience.config;

import com.accurascience.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;

/**
 * spring security配置
 *
 * @author zhuchaojie
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserService userService;

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

//	    @Override
//	    @Bean
//	    public UserDetailsService userDetailsServiceBean() throws Exception {
//	        return super.userDetailsServiceBean();
//	    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//	        auth.inMemoryAuthentication()
//	                .withUser("user")
//	                .password("123456")
//	                .authorities("ROLE_USER")
//	                .and()
//	                .withUser("admin")
//	                .password("123456")
//	                .authorities("ROLE_USER", "ROLE_ADMIN");
        auth.userDetailsService(userService)/*加载用户信息*/.passwordEncoder(new BCryptPasswordEncoder())/*密码加密*/;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .authorizeRequests()
                .antMatchers("/oauth/**", "/login/**", "/logout/**", "/password/**")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .permitAll();
    }

    /**
     * test
     *
     * @param args
     */
    public static void main(String[] args) {
        Jwt jwt = JwtHelper.decode("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJ0ZXN0Iiwic2NvcGUiOlsiYWxsIl" +
                "0sImlkIjoiODMyOGE1Mjg1MDY5NGM1MGIyYzI5YThkNTRiYjI3ZWMiLCJleHAiOjE1ODgxMTEzMzIsImF1dGhvcml0aWVzIjpb" +
                "ImFkbWluIl0sImp0aSI6IjliZjdiNGE5LTI5MmUtNGZmYi1iNDczLWIyYjc2NWZmZmIyZSIsImVtYWlsIjoiIiwiY2xpZW50X2l" +
                "kIjoid2hvbGUtZXhvbWUifQ.Qm1PYb76VPdE8RxlIS0mL0r_8h1AIxTu53VeAamEVz8");
        System.out.println(jwt.getClaims());
        //System.out.println(new BCryptPasswordEncoder().encode("accura-science-2020.03.31"));
    }
}
