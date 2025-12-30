package com.logistics.rbacbackend.config;

import com.logistics.rbacbackend.dao.RbacQueryDao;
import com.logistics.rbacbackend.utils.JwtAuthFilter;
import com.logistics.rbacbackend.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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

    /*    @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                    .csrf(csrf -> csrf.disable()) // 前后端分离通常先关 CSRF，方便 Postman/前端调试
                    .authorizeHttpRequests(auth -> auth
                                    .anyRequest().permitAll()  // 所有接口放行
                                          )
                    .formLogin(form -> form.disable()) // 关闭默认登录页
                    .httpBasic(Customizer.withDefaults());

            return http.build();
        }*/
    @Bean
    public JwtTokenUtil jwtTokenUtil(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expireSeconds}") long expireSeconds) {
        return new JwtTokenUtil(secret, expireSeconds);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           JwtTokenUtil jwtTokenUtil,
                                           RbacQueryDao rbacQueryDao) throws Exception {

        JwtAuthFilter jwtAuthFilter = new JwtAuthFilter(jwtTokenUtil, rbacQueryDao);

        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                // 关键：禁用默认登录页/Basic，否则就会跳 /login
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())

                // 无状态
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 未登录/无权限时返回 JSON（避免重定向）
                .exceptionHandling(eh -> eh
                                .authenticationEntryPoint((req, resp, ex) ->
                                {
                                    resp.setStatus(401);
                                    resp.setContentType(MediaType.APPLICATION_JSON_VALUE);
                                    resp.getWriter().write("{\"code\":401,\"msg\":\"未登录或token无效\",\"data\":null}");
                                })
                                .accessDeniedHandler((req, resp, ex) ->
                                {
                                    resp.setStatus(403);
                                    resp.setContentType(MediaType.APPLICATION_JSON_VALUE);
                                    resp.getWriter().write("{\"code\":403,\"msg\":\"无权限\",\"data\":null}");
                                })
                                  )

                .authorizeHttpRequests(auth -> auth
                                // 登录放行
                                .requestMatchers("/api/auth/login").permitAll()

                                // RBAC 只有 super_admin
                                .requestMatchers("/api/rbac/**").hasRole("super_admin")

                                // 你也可以顺手把系统管理接口一起限制（可选）
                                .requestMatchers("/api/users/**", "/api/roles/**", "/api/permissions/**").hasRole("super_admin")

                                // 其他接口：先全部需要登录
                                .anyRequest().authenticated()
                                      )

                // JWT filter 放在 UsernamePasswordAuthenticationFilter 之前
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
