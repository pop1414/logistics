package com.logistics.rbacbackend.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author DDY
 * @version 1.0
 * @date 2025/12/30-22:50
 * @description com.logistics.rbacbackend.utils
 */
public class TempBCrypt {
    public static void main(String[] args) {
        System.out.println(new BCryptPasswordEncoder().encode("admin"));
    }
}
