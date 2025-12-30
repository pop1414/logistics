package com.logistics.rbacbackend.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.logistics.rbacbackend.dao.RbacQueryDao;
import com.logistics.rbacbackend.dto.RoleCreateDTO;
import com.logistics.rbacbackend.dto.RoleUpdateDTO;
import com.logistics.rbacbackend.mbg.mapper.RolesMapper;
import com.logistics.rbacbackend.mbg.model.Roles;
import com.logistics.rbacbackend.mbg.model.RolesExample;
import com.logistics.rbacbackend.service.RoleService;
import com.logistics.rbacbackend.vo.RoleVO;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author DDY
 * @version 1.0
 * @date 2025/12/30-11:34
 * @description com.logistics.rbacbackend.service.impl
 */
@Service
public class RoleServiceImpl implements RoleService {
    private final RolesMapper rolesMapper;
    private final RbacQueryDao rbacQueryMapper;

    public RoleServiceImpl(RolesMapper rolesMapper, RbacQueryDao rbacQueryMapper) {
        this.rolesMapper = rolesMapper;
        this.rbacQueryMapper = rbacQueryMapper;
    }

    @Override
    @Transactional
    public Long createRole(RoleCreateDTO dto) {
        if (dto == null || dto.getRoleName() == null || dto.getRoleName().isBlank()) {
            throw new IllegalArgumentException("roleName 不能为空");
        }

        Roles r = new Roles();
        r.setRoleName(dto.getRoleName().trim());
        r.setDescription(dto.getDescription());
        // 新建角色默认非初始化角色
        // 注意：MBG 生成的类型可能是 Boolean/Byte/Integer
        // 这里用 Integer 0 作为示例，按你实际类型调整
        setIsInitial(r);

        try {
            int rows = rolesMapper.insert(r);
            if (rows != 1) {
                throw new IllegalStateException("创建角色失败");
            }
        } catch (DuplicateKeyException e) {
            throw new IllegalArgumentException("角色名已存在: " + dto.getRoleName());
        }

        if (r.getId() == null) {
            throw new IllegalStateException("创建角色成功但未回填ID，请检查MBG generatedKey 配置");
        }
        return r.getId();
    }

    @Override
    @Transactional
    public void updateRole(Long roleId, RoleUpdateDTO dto) {
        if (roleId == null) {
            throw new IllegalArgumentException("roleId 不能为空");
        }
        if (dto == null || dto.getRoleName() == null || dto.getRoleName().isBlank()) {
            throw new IllegalArgumentException("roleName 不能为空");
        }

        Roles db = rolesMapper.selectByPrimaryKey(roleId);
        if (db == null) {
            throw new IllegalArgumentException("角色不存在 roleId=" + roleId);
        }

        if (isInitial(db)) {
            throw new IllegalArgumentException("初始化角色不可修改");
        }

        Roles patch = new Roles();
        patch.setId(roleId);
        patch.setRoleName(dto.getRoleName().trim());
        patch.setDescription(dto.getDescription());

        try {
            rolesMapper.updateByPrimaryKeySelective(patch);
        } catch (DuplicateKeyException e) {
            throw new IllegalArgumentException("角色名已存在: " + dto.getRoleName());
        }
    }

    @Override
    @Transactional
    public void deleteRole(Long roleId) {
        if (roleId == null) {
            throw new IllegalArgumentException("roleId 不能为空");
        }

        Roles db = rolesMapper.selectByPrimaryKey(roleId);
        if (db == null) {
            return; // 或抛错都行
        }

        if (isInitial(db)) {
            throw new IllegalArgumentException("初始化角色不可删除");
        }

        rolesMapper.deleteByPrimaryKey(roleId);
        // 你有外键 role_permissions / user_roles ON DELETE CASCADE，所以关联会自动删
    }

    @Override
    public PageInfo<RoleVO> listRoles(int pageNum, int pageSize, String roleNameKeyword) {
        PageHelper.startPage(pageNum, pageSize);

        RolesExample ex = new RolesExample();
        RolesExample.Criteria c = ex.createCriteria();
        if (roleNameKeyword != null && !roleNameKeyword.isBlank()) {
            c.andRoleNameLike("%" + roleNameKeyword.trim() + "%");
        }
        ex.setOrderByClause("id desc");

        List<Roles> list = rolesMapper.selectByExample(ex);

        List<RoleVO> voList = list.stream().map(this::toVOWithoutPerms).toList();
        PageInfo<Roles> page = new PageInfo<>(list);

        PageInfo<RoleVO> out = new PageInfo<>();
        out.setList(voList);
        out.setPageNum(page.getPageNum());
        out.setPageSize(page.getPageSize());
        out.setTotal(page.getTotal());
        out.setPages(page.getPages());
        out.setHasNextPage(page.isHasNextPage());
        out.setHasPreviousPage(page.isHasPreviousPage());
        return out;
    }

    @Override
    public RoleVO getRoleDetail(Long roleId) {
        if (roleId == null) {
            throw new IllegalArgumentException("roleId 不能为空");
        }

        Roles db = rolesMapper.selectByPrimaryKey(roleId);
        if (db == null) {
            throw new IllegalArgumentException("角色不存在 roleId=" + roleId);
        }

        RoleVO vo = toVOWithoutPerms(db);

        List<Long> permIds = rbacQueryMapper.selectPermIdsByRoleId(roleId);
        vo.setPermIds(permIds == null ? Collections.emptyList() : permIds);

        return vo;
    }

    @Override
    public List<RoleVO> listAllRoles() {
        RolesExample ex = new RolesExample();
        ex.setOrderByClause("id asc");
        List<Roles> list = rolesMapper.selectByExample(ex);
        return list.stream().map(this::toVOWithoutPerms).toList();
    }

    private RoleVO toVOWithoutPerms(Roles r) {
        RoleVO vo = new RoleVO();
        vo.setId(r.getId());
        vo.setRoleName(r.getRoleName());
        vo.setDescription(r.getDescription());
        vo.setCreateTime(r.getCreateTime());

        // isInitial 类型不确定，统一转成 Integer
        vo.setIsInitial(isInitial(r));
        return vo;
    }

    // ====== 下面三段是“兼容MBG字段类型差异”的小工具 ======
    // 你的 Roles.isInitial 字段可能是 Boolean / Byte / Integer
    private boolean isInitial(Roles r) {
        try {
            Object v = r.getIsInitial();
            if (v == null) {
                return false;
            }
            if (v instanceof Boolean b) {
                return b;
            }
            if (v instanceof Byte b) {
                return b != 0;
            }
            if (v instanceof Integer i) {
                return i != 0;
            }
            return Objects.equals(v.toString(), "1") || Objects.equals(v.toString(), "true");
        } catch (Exception e) {
            // 如果你的MBG字段名不是 getIsInitial，请按实际改这里
            return false;
        }
    }

    private void setIsInitial(Roles r) {
        try {
            // 如果你的MBG类型是 Byte/Boolean，请按生成代码改
            r.setIsInitial(false);
        } catch (Exception e) {
            // 若 setIsInitial 签名不同，你就直接按 Roles.java 的实际类型写
            // 比如：r.setIsInitial(false); 或 r.setIsInitial(v);
        }
    }
}
