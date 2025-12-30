package com.logistics.rbacbackend.dto;

import java.util.List;

/**
 * @author DDY
 * @version 1.0
 * @date 2025/12/30-10:02
 * @description com.logistics.rbacbackend.dto
 */
public class UserAssignRolesDTO {
    private List<Long> roleIds;

    public List<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Long> roleIds) {
        this.roleIds = roleIds;
    }
}
