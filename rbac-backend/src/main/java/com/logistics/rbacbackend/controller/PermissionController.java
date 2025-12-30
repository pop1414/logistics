package com.logistics.rbacbackend.controller;

import com.logistics.rbacbackend.common.ApiResponse;
import com.logistics.rbacbackend.mbg.mapper.PermissionsMapper;
import com.logistics.rbacbackend.mbg.model.Permissions;
import com.logistics.rbacbackend.mbg.model.PermissionsExample;
import com.logistics.rbacbackend.vo.PermissionVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author DDY
 * @version 1.0
 * @date 2025/12/30-15:38
 * @description com.logistics.rbacbackend.controller
 */
@RestController
@RequestMapping("/api/permissions")
public class PermissionController {
    private final PermissionsMapper permissionsMapper;

    public PermissionController(PermissionsMapper permissionsMapper) {
        this.permissionsMapper = permissionsMapper;
    }

    /**
     * 权限选项列表（给“配置角色权限”页面用）
     * GET /api/permissions
     */
    @GetMapping
    public ApiResponse<List<PermissionVO>> list() {
        PermissionsExample ex = new PermissionsExample();
        ex.setOrderByClause("module, perm_name");

        List<Permissions> list = permissionsMapper.selectByExample(ex);

        List<PermissionVO> voList = new ArrayList<>();
        for (Permissions p : list) {
            voList.add(toVO(p));
        }
        return ApiResponse.ok(voList);
    }

    private PermissionVO toVO(Permissions p) {
        PermissionVO vo = new PermissionVO();
        vo.setId(p.getId());
        vo.setPermName(p.getPermName());
        vo.setDescription(p.getDescription());
        vo.setModule(p.getModule());
        vo.setCreateTime(p.getCreateTime());
        return vo;
    }
}
