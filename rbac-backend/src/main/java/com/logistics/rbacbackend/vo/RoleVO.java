package com.logistics.rbacbackend.vo;

import java.util.Date;
import java.util.List;

/**
 * @author DDY
 * @version 1.0
 * @date 2025/12/30-10:03
 * @description com.logistics.rbacbackend.vo
 */
public class RoleVO {
    private Long id;
    private String roleName;
    private String description;
    private Boolean isInitial;
    private Date createTime;

    // 回显权限（不在 roles 表）
    private List<Long> permIds;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Boolean getIsInitial() {
        return isInitial;
    }

    public void setIsInitial(Boolean isInitial) {
        this.isInitial = isInitial;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public List<Long> getPermIds() {
        return permIds;
    }

    public void setPermIds(List<Long> permIds) {
        this.permIds = permIds;
    }
}
