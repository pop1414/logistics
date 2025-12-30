package com.logistics.rbacbackend.service.impl;

import com.logistics.rbacbackend.dao.RbacQueryDao;
import com.logistics.rbacbackend.mbg.mapper.RolePermissionsMapper;
import com.logistics.rbacbackend.mbg.mapper.UserRolesMapper;
import com.logistics.rbacbackend.mbg.mapper.UsersMapper;
import com.logistics.rbacbackend.mbg.model.*;
import com.logistics.rbacbackend.service.RbacService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author DDY
 * @version 1.0
 * @date 2025/12/30-10:32
 */
@Service
public class RbacServiceImpl implements RbacService {
    private final UsersMapper usersMapper;
    private final UserRolesMapper userRolesMapper;
    private final RolePermissionsMapper rolePermissionsMapper;
    private final RbacQueryDao rbacQueryMapper;

    public RbacServiceImpl(UsersMapper usersMapper,
                           UserRolesMapper userRolesMapper,
                           RolePermissionsMapper rolePermissionsMapper,
                           RbacQueryDao rbacQueryMapper) {
        this.usersMapper = usersMapper;
        this.userRolesMapper = userRolesMapper;
        this.rolePermissionsMapper = rolePermissionsMapper;
        this.rbacQueryMapper = rbacQueryMapper;
    }

    /**
     * 1) 给用户分配角色：先删旧，再插新（覆盖式保存）
     */
    @Override
    @Transactional
    public void assignRolesToUser(Long userId, List<Long> roleIds) {
        if (userId == null) {
            throw new IllegalArgumentException("userId 不能为空");
        }
        Users u = usersMapper.selectByPrimaryKey(userId);
        if (u == null) {
            throw new IllegalArgumentException("用户不存在 userId=" + userId);
        }

        // 1. 删除旧关联
        UserRolesExample delEx = new UserRolesExample();
        delEx.createCriteria().andUserIdEqualTo(userId);
        userRolesMapper.deleteByExample(delEx);

        // 2. 允许清空（roleIds 为空表示清空角色）
        if (roleIds == null || roleIds.isEmpty()) {
            return;
        }

        // 3. 去掉 null + 去重
        List<Long> cleaned = roleIds.stream()
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        if (cleaned.isEmpty()) {
            return;
        }

        // 4. 插入新关联
        for (Long roleId : cleaned) {
            UserRoles ur = new UserRoles();
            ur.setUserId(userId);
            ur.setRoleId(roleId);
            userRolesMapper.insert(ur);
        }
    }

    /**
     * 2) 给角色分配权限：先删旧，再插新（覆盖式保存）
     */
    @Override
    @Transactional
    public void assignPermsToRole(Long roleId, List<Long> permIds) {
        if (roleId == null) {
            throw new IllegalArgumentException("roleId 不能为空");
        }

        // 1. 删除旧关联
        RolePermissionsExample delEx = new RolePermissionsExample();
        delEx.createCriteria().andRoleIdEqualTo(roleId);
        rolePermissionsMapper.deleteByExample(delEx);

        // 2. 允许清空
        if (permIds == null || permIds.isEmpty()) {
            return;
        }

        // 3. 去掉 null + 去重
        List<Long> cleaned = permIds.stream()
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        if (cleaned.isEmpty()) {
            return;
        }

        // 4. 插入新关联
        for (Long permId : cleaned) {
            RolePermissions rp = new RolePermissions();
            rp.setRoleId(roleId);
            rp.setPermId(permId);
            rolePermissionsMapper.insert(rp);
        }
    }

    /**
     * 3) 查用户权限码列表：联表查询（用于鉴权）
     */
    @Override
    public List<String> getUserPermCodes(Long userId) {
        if (userId == null) {
            return Collections.emptyList();
        }
        return rbacQueryMapper.selectPermCodesByUserId(userId);
    }
}
