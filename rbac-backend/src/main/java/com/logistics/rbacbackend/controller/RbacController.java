package com.logistics.rbacbackend.controller;

import com.logistics.rbacbackend.common.ApiResponse;
import com.logistics.rbacbackend.dto.RoleAssignPermsDTO;
import com.logistics.rbacbackend.dto.UserAssignRolesDTO;
import com.logistics.rbacbackend.service.RbacService;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * @author DDY
 * @version 1.0
 * @date 2025/12/30-16:44
 * @description com.logistics.rbacbackend.controller
 */
@RestController
@RequestMapping("/api/rbac")
public class RbacController {
    private final RbacService rbacService;

    public RbacController(RbacService rbacService) {
        this.rbacService = rbacService;
    }

    /**
     * 给用户分配角色（覆盖式）
     */
    @PutMapping("/users/{userId}/roles")
    public ApiResponse<Void> assignRolesToUser(@PathVariable Long userId,
                                               @RequestBody(required = false) UserAssignRolesDTO dto) {
        List<Long> roleIds = (dto == null || dto.getRoleIds() == null)
                ? Collections.emptyList()
                : dto.getRoleIds();
        rbacService.assignRolesToUser(userId, roleIds);
        return ApiResponse.ok();
    }

    /**
     * 给角色分配权限（覆盖式）
     */
    @PutMapping("/roles/{roleId}/perms")
    public ApiResponse<Void> assignPermsToRole(@PathVariable Long roleId,
                                               @RequestBody(required = false) RoleAssignPermsDTO dto) {
        List<Long> permIds = (dto == null || dto.getPermIds() == null)
                ? Collections.emptyList()
                : dto.getPermIds();
        rbacService.assignPermsToRole(roleId, permIds);
        return ApiResponse.ok();
    }

    /**
     * 查询用户最终权限码
     */
    @GetMapping("/users/{userId}/perm-codes")
    public ApiResponse<List<String>> getUserPermCodes(@PathVariable Long userId) {
        return ApiResponse.ok(rbacService.getUserPermCodes(userId));
    }
}
