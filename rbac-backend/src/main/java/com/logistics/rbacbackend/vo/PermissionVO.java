package com.logistics.rbacbackend.vo;

import java.util.Date;

/**
 * @author DDY
 * @version 1.0
 * @date 2025/12/30-10:03
 * @description com.logistics.rbacbackend.vo
 */
public class PermissionVO {
    private Long id;
    private String permName;
    private String description;
    private String module;
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPermName() {
        return permName;
    }

    public void setPermName(String permName) {
        this.permName = permName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
