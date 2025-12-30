package com.logistics.rbacbackend.controller;

import com.github.pagehelper.PageInfo;
import com.logistics.rbacbackend.common.ApiResponse;
import com.logistics.rbacbackend.dto.UserAssignRolesDTO;
import com.logistics.rbacbackend.dto.UserCreateDTO;
import com.logistics.rbacbackend.dto.UserUpdateDTO;
import com.logistics.rbacbackend.dto.UserUpdatePasswordDTO;
import com.logistics.rbacbackend.service.RbacService;
import com.logistics.rbacbackend.service.UserService;
import com.logistics.rbacbackend.vo.UserVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author DDY
 * @version 1.0
 * @date 2025/12/30-14:41
 * @description com.logistics.rbacbackend.controller
 */
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final RbacService rbacService;

    public UserController(UserService userService, RbacService rbacService) {
        this.userService = userService;
        this.rbacService = rbacService;
    }

    /**
     * 创建用户（只写 users 表）
     * 角色分配建议走：PUT /api/users/{id}/roles
     */
    @PostMapping
    public ApiResponse<Long> create(@RequestBody UserCreateDTO dto) {
        Long userId = userService.createUser(dto);
        return ApiResponse.ok(userId);
    }

    /**
     * 更新用户基础信息（如 email）
     */
    @PutMapping("/{userId}")
    public ApiResponse<Void> update(@PathVariable Long userId,
                                    @RequestBody UserUpdateDTO dto) {
        userService.updateUser(userId, dto);
        return ApiResponse.ok();
    }

    /**
     * 修改密码（校验旧密码）
     */
    @PutMapping("/{userId}/password")
    public ApiResponse<Void> updatePassword(@PathVariable Long userId,
                                            @RequestBody UserUpdatePasswordDTO dto) {
        userService.updatePassword(userId, dto);
        return ApiResponse.ok();
    }

    /**
     * 用户列表（分页，可按 username 模糊搜索）
     * GET /api/users?pageNum=1&pageSize=10&username=ali
     */
    @GetMapping
    public ApiResponse<PageInfo<UserVO>> list(@RequestParam(defaultValue = "1") int pageNum,
                                              @RequestParam(defaultValue = "10") int pageSize,
                                              @RequestParam(required = false) String username) {
        return ApiResponse.ok(userService.listUsers(pageNum, pageSize, username));
    }

    /**
     * 用户详情（包含 roleIds 回显）
     */
    @GetMapping("/{userId}")
    public ApiResponse<UserVO> detail(@PathVariable Long userId) {
        return ApiResponse.ok(userService.getUserDetail(userId));
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{userId}")
    public ApiResponse<Void> delete(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ApiResponse.ok();
    }

    // =========================
    // RBAC：给用户分配角色（覆盖式保存）
    // =========================

    /**
     * 给用户分配角色：先删旧，再插新（roleIds 为空表示清空）
     * PUT /api/users/{id}/roles  body: {"roleIds":[1,2,3]}
     */
    @PutMapping("/{userId}/roles")
    public ApiResponse<Void> assignRoles(@PathVariable Long userId,
                                         @RequestBody UserAssignRolesDTO dto) {
        List<Long> roleIds = (dto == null ? null : dto.getRoleIds());
        rbacService.assignRolesToUser(userId, roleIds);
        return ApiResponse.ok();
    }

}
