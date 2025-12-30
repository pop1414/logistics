package com.logistics.rbacbackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * @author DDY
 * @version 1.0
 * @date 2025/12/30-11:28
 * @description com.logistics.rbacbackend.dto
 */
public class RoleUpdateDTO {
    @NotBlank
    @Size(max = 50)
    private String roleName;

    @Size(max = 255)
    private String description;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
