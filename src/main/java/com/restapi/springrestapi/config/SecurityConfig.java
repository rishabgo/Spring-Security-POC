package com.restapi.springrestapi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String ADMIN_ROLE = "ADMIN";
    public static final String USER_ROLE = "USER";
    public static final String POST_PATH = "/api/v1/posts/**";
    public static final String USERS_PATH = "/users/**";
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.DELETE, USERS_PATH, POST_PATH).hasRole(ADMIN_ROLE)
                .antMatchers(HttpMethod.GET, USERS_PATH, POST_PATH).hasAnyRole(USER_ROLE, ADMIN_ROLE)
                .antMatchers(HttpMethod.POST, POST_PATH).hasAnyRole(USER_ROLE, ADMIN_ROLE)
                .antMatchers(HttpMethod.PUT, POST_PATH).hasAnyRole(USER_ROLE, ADMIN_ROLE)
                .antMatchers("/h2-console/**").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();

        http.csrf().disable();
        http.cors().disable();
        http.headers().frameOptions().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(HttpMethod.POST, "/users")
                .antMatchers("/v3/api-docs/**",
                "/swagger-ui/**");
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}

