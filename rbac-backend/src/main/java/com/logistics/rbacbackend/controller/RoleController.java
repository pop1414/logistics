package com.logistics.rbacbackend.controller;

import com.github.pagehelper.PageInfo;
import com.logistics.rbacbackend.common.ApiResponse;
import com.logistics.rbacbackend.dto.RoleCreateDTO;
import com.logistics.rbacbackend.dto.RoleUpdateDTO;
import com.logistics.rbacbackend.service.RoleService;
import com.logistics.rbacbackend.vo.RoleVO;
import org.springframework.web.bind.annotation.*;

/**
 * @author DDY
 * @version 1.0
 * @date 2025/12/30-15:46
 * @description com.logistics.rbacbackend.controller
 */
@RestController
@RequestMapping("/api/roles")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * 创建角色
     */
    @PostMapping
    public ApiResponse<Long> create(@RequestBody RoleCreateDTO dto) {
        Long roleId = roleService.createRole(dto);
        return ApiResponse.ok(roleId);
    }

    /**
     * 更新角色
     */
    @PutMapping("/{roleId}")
    public ApiResponse<Void> update(@PathVariable Long roleId,
                                    @RequestBody RoleUpdateDTO dto) {
        roleService.updateRole(roleId, dto);
        return ApiResponse.ok();
    }

    /**
     * 删除角色（初始化角色不可删：这个限制建议在 RoleServiceImpl 内做）
     */
    @DeleteMapping("/{roleId}")
    public ApiResponse<Void> delete(@PathVariable Long roleId) {
        roleService.deleteRole(roleId);
        return ApiResponse.ok();
    }

    /**
     * 角色分页列表（可模糊搜索 roleName）
     */
    @GetMapping
    public ApiResponse<PageInfo<RoleVO>> list(@RequestParam(defaultValue = "1") int pageNum,
                                              @RequestParam(defaultValue = "10") int pageSize,
                                              @RequestParam(required = false) String roleName) {
        return ApiResponse.ok(roleService.listRoles(pageNum, pageSize, roleName));
    }

    /**
     * 角色详情（一般需要回显 permIds：如果你 RoleVO 有 permIds，就在 Service 里填好）
     */
    @GetMapping("/{roleId}")
    public ApiResponse<RoleVO> detail(@PathVariable Long roleId) {
        return ApiResponse.ok(roleService.getRoleDetail(roleId));
    }
}























