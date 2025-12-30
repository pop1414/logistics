package com.logistics.rbacbackend.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;


/**
 * @author DDY
 * @version 1.0
 * @date 2025/12/30-22:19
 * @description com.logistics.rbacbackend.utils
 */
public class JwtTokenUtil {

    private final SecretKey key;
    private final long expireMillis;

    public JwtTokenUtil(String secret, long expireSeconds) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expireMillis = expireSeconds * 1000L;
    }

    public String generateToken(Long userId, String username) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + expireMillis);

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("username", username)
                .issuedAt(now)
                .expiration(exp)
                .signWith(key)
                .compact();
    }

    public Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Long getUserId(String token) {
        Claims c = parseClaims(token);
        return Long.valueOf(c.getSubject());
    }

    public String getUsername(String token) {
        Claims c = parseClaims(token);
        Object u = c.get("username");
        return u == null ? null : String.valueOf(u);
    }
}

