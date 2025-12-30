package com.logistics.rbacbackend.dto;

/**
 * @author DDY
 * @version 1.0
 * @date 2025/12/30-22:22
 * @description com.logistics.rbacbackend.dto
 */
public class LoginDTO {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
