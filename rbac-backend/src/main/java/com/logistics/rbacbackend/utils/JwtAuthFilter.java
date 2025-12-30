package com.logistics.rbacbackend.utils;

import com.logistics.rbacbackend.dao.RbacQueryDao;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * @author DDY
 * @version 1.0
 * @date 2025/12/30-22:21
 * @description com.logistics.rbacbackend.utils
 */
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final RbacQueryDao rbacQueryDao;

    public JwtAuthFilter(JwtTokenUtil jwtTokenUtil, RbacQueryDao rbacQueryDao) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.rbacQueryDao = rbacQueryDao;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String auth = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.hasText(auth) && auth.startsWith("Bearer ")) {
            String token = auth.substring(7);

            try {
                Long userId = jwtTokenUtil.getUserId(token);
                String username = jwtTokenUtil.getUsername(token);

                // 取用户角色，转成 Spring Security 的 ROLE_*
                List<String> roleNames = rbacQueryDao.selectRoleNamesByUserId(userId);
                var authorities = roleNames.stream()
                        .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                        .toList();

                // principal 可以放 username，也可以放自定义对象（这里先简单）
                var authentication = new UsernamePasswordAuthenticationToken(
                        username, null, authorities
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (JwtException | IllegalArgumentException e) {
                // token 不合法：不设置认证，让后续 Security 走 401
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);
    }
}

