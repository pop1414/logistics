package com.logistics.rbacbackend.dto;

import jakarta.validation.constraints.Size;

/**
 * @author DDY
 * @version 1.0
 * @date 2025/12/30-10:40
 * @description com.logistics.rbacbackend.dto
 */
public class UserUpdateDTO {
    @Size(max = 100)
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
