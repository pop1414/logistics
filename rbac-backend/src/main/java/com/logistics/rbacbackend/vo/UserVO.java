package com.logistics.rbacbackend.vo;

import java.util.Date;
import java.util.List;

/**
 * @author DDY
 * @version 1.0
 * @date 2025/12/30-10:03
 * @description com.logistics.rbacbackend.vo
 */
public class UserVO {
    private Long id;
    private String username;
    private String email;
    private Date createTime;
    private Date updateTime;

    // RBAC 回显字段：不在 users 表里
    private List<Long> roleIds;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public List<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Long> roleIds) {
        this.roleIds = roleIds;
    }
}
