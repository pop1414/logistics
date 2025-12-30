package com.logistics.rbacbackend.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author DDY
 * @version 1.0
 * @date 2025/12/30-10:08
 */
public interface RbacQueryDao {
    /**
     * 根据用户ID查询权限码（perm_name）列表
     */
    List<String> selectPermCodesByUserId(@Param("userId") Long userId);

    /**
     * 根据用户ID查询角色ID列表
     */
    List<Long> selectRoleIdsByUserId(@Param("userId") Long userId);

    /**
     * 根据角色ID查询权限ID列表
     */
    List<Long> selectPermIdsByRoleId(@Param("roleId") Long roleId);
}
