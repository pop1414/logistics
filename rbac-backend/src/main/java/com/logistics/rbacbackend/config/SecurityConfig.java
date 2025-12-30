package com.logistics.rbacbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
}
