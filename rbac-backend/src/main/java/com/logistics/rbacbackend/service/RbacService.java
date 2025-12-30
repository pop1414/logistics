package com.logistics.rbacbackend.service;

import java.util.List;

/**
 * @author DDY
 * @version 1.0
 * @date 2025/12/30-10:16
 */
public interface RbacService {
    /**
     * 给用户分配角色（覆盖式保存）
     */
    void assignRolesToUser(Long userId, List<Long> roleIds);

    /**
     * 给角色分配权限（覆盖式保存）
     */
    void assignPermsToRole(Long roleId, List<Long> permIds);

    /**
     * 查用户权限码列表（用于鉴权）
     */
    List<String> getUserPermCodes(Long userId);

    /**
     * 查角色权限码列表（用于回显）
     */
    List<Long> getPermIdsByRoleId(Long roleId);

}
