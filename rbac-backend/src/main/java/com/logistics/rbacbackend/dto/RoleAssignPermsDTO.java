package com.logistics.rbacbackend.dto;

import java.util.List;

/**
 * @author DDY
 * @version 1.0
 * @date 2025/12/30-10:02
 * @description com.logistics.rbacbackend.dto
 */
public class RoleAssignPermsDTO {
    private List<Long> permIds;

    public List<Long> getPermIds() {
        return permIds;
    }

    public void setPermIds(List<Long> permIds) {
        this.permIds = permIds;
    }
}
