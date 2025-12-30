package com.logistics.rbacbackend.vo;

import java.util.List;

/**
 * @author DDY
 * @version 1.0
 * @date 2025/12/30-22:23
 * @description com.logistics.rbacbackend.vo
 */
public class LoginVO {
    private String token;
    private Long userId;
    private String username;
    private List<String> roleNames;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRoleNames() {
        return roleNames;
    }

    public void setRoleNames(List<String> roleNames) {
        this.roleNames = roleNames;
    }
}
