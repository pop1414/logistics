package com.logistics.rbacbackend.service;

import com.github.pagehelper.PageInfo;
import com.logistics.rbacbackend.mbg.model.Roles;

import java.util.List;

/**
 * @author DDY
 * @version 1.0
 * @date 2025/12/30-9:48
 * @description com.logistics.rbacbackend.service
 */
public interface RolesService {
    /**
     * 创建角色
     *
     * @param role 角色对象
     * @return 创建后的角色ID
     */
    Long createRole(Roles role);

    /**
     * 更新角色信息
     *
     * @param role 更新对象
     */
    void updateRole(Roles role);

    /**
     * 删除角色（检查是否初始化角色）
     *
     * @param roleId 角色ID
     */
    void deleteRole(Long roleId);

    /**
     * 根据ID查询角色（包括权限）
     *
     * @param roleId 角色ID
     * @return Roles 对象，带权限列表
     */
    Roles getRoleById(Long roleId);

    /**
     * 分页查询角色列表
     *
     * @param pageNum  页码
     * @param pageSize 页大小
     * @param roleName 可选搜索角色名
     * @return PageInfo<Roles>
     */
    PageInfo<Roles> listRoles(int pageNum, int pageSize, String roleName);

    /**
     * 配置角色权限（支持多个权限）
     *
     * @param roleId  角色ID
     * @param permIds 权限ID列表
     */
    void assignPermissions(Long roleId, List<Long> permIds);

    /**
     * 获取角色权限ID列表
     *
     * @param roleId 角色ID
     * @return List<Long> 权限ID
     */
    List<Long> getRolePermissions(Long roleId);
}
