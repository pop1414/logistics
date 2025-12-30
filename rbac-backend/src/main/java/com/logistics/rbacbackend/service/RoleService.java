package com.logistics.rbacbackend.service;

import com.github.pagehelper.PageInfo;
import com.logistics.rbacbackend.dto.RoleCreateDTO;
import com.logistics.rbacbackend.dto.RoleUpdateDTO;
import com.logistics.rbacbackend.vo.RoleVO;

import java.util.List;

/**
 * @author DDY
 * @version 1.0
 * @date 2025/12/30-9:48
 * @description com.logistics.rbacbackend.service
 */
public interface RoleService {
    Long createRole(RoleCreateDTO dto);

    void updateRole(Long roleId, RoleUpdateDTO dto);

    void deleteRole(Long roleId);

    PageInfo<RoleVO> listRoles(int pageNum, int pageSize, String roleNameKeyword);

    RoleVO getRoleDetail(Long roleId);

    // 可选：不分页版本（给下拉框用）
    List<RoleVO> listAllRoles();
}
