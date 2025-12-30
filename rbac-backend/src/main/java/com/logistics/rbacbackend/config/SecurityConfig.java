package com.logistics.rbacbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author DDY
 * @version 1.0
 * @date 2025/12/30-9:33
 */
@Configuration
public class SecurityConfig {
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {  // 方法名可以是 passwordEncoder 或 bCryptPasswordEncoder
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // 前后端分离通常先关 CSRF，方便 Postman/前端调试
                .authorizeHttpRequests(auth -> auth
                                .anyRequest().permitAll()  // 所有接口放行
                                      )
                .formLogin(form -> form.disable()) // 关闭默认登录页
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
