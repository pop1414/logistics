package com.logistics.rbacbackend.service;


import com.github.pagehelper.PageInfo;
import com.logistics.rbacbackend.dto.RoleCreateDTO;
import com.logistics.rbacbackend.dto.RoleUpdateDTO;
import com.logistics.rbacbackend.mbg.mapper.RolesMapper;
import com.logistics.rbacbackend.mbg.model.Roles;
import com.logistics.rbacbackend.vo.RoleVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;


/**
 * @author DDY
 * @version 1.0
 * @date 2025/12/30-13:08
 * @description com.logistics.rbacbackend.mgb.service
 */

@SpringBootTest
@Transactional
@Rollback
class RoleServiceTest {

    @Autowired
    private RoleService roleService;

    @Autowired
    private RolesMapper rolesMapper;

    private String uniq(String prefix) {
        return prefix + System.currentTimeMillis();
    }

    @Test
    void createRole_shouldInsert() {
        RoleCreateDTO dto = new RoleCreateDTO();
        dto.setRoleName(uniq("role_"));
        dto.setDescription("test role");

        Long roleId = roleService.createRole(dto);
        assertNotNull(roleId);

        Roles db = rolesMapper.selectByPrimaryKey(roleId);
        assertNotNull(db);
        assertEquals(dto.getRoleName(), db.getRoleName());
        assertEquals(dto.getDescription(), db.getDescription());
    }

    @Test
    void createRole_duplicateRoleName_shouldThrow() {
        String name = uniq("dup_role_");

        RoleCreateDTO dto1 = new RoleCreateDTO();
        dto1.setRoleName(name);
        dto1.setDescription("a");
        roleService.createRole(dto1);

        RoleCreateDTO dto2 = new RoleCreateDTO();
        dto2.setRoleName(name);
        dto2.setDescription("b");

        assertThrows(IllegalArgumentException.class, () -> roleService.createRole(dto2));
    }

    @Test
    void updateRole_shouldUpdate_whenNotInitial() {
        RoleCreateDTO dto = new RoleCreateDTO();
        dto.setRoleName(uniq("upd_role_"));
        dto.setDescription("old");
        Long roleId = roleService.createRole(dto);

        RoleUpdateDTO upd = new RoleUpdateDTO();
        upd.setRoleName(dto.getRoleName() + "_new");
        upd.setDescription("new");

        roleService.updateRole(roleId, upd);

        Roles db = rolesMapper.selectByPrimaryKey(roleId);
        assertEquals(upd.getRoleName(), db.getRoleName());
        assertEquals(upd.getDescription(), db.getDescription());
    }

    @Test
    void deleteRole_shouldDelete_whenNotInitial() {
        RoleCreateDTO dto = new RoleCreateDTO();
        dto.setRoleName(uniq("del_role_"));
        dto.setDescription("to delete");
        Long roleId = roleService.createRole(dto);

        roleService.deleteRole(roleId);
        Roles db = rolesMapper.selectByPrimaryKey(roleId);
        assertNull(db);
    }

    @Test
    void deleteRole_shouldThrow_whenInitialRole() {
        // 不依赖你预插数据：我们直接用 rolesMapper 插入一个 is_initial=1 的角色
        Roles r = new Roles();
        r.setRoleName(uniq("init_role_"));
        r.setDescription("initial role");

        // 注意：这里按你的 Roles.java 实际类型调整
        // Byte: r.setIsInitial((byte)1);
        // Boolean: r.setIsInitial(true);
        // Integer: r.setIsInitial(1);
        r.setIsInitial(true);

        rolesMapper.insert(r);
        assertNotNull(r.getId());

        assertThrows(IllegalArgumentException.class, () -> roleService.deleteRole(r.getId()));
    }

    @Test
    void listRoles_shouldSupportLikeQuery() {
        for (int i = 0; i < 3; i++) {
            RoleCreateDTO dto = new RoleCreateDTO();
            dto.setRoleName(uniq("list_role_") + "_" + i);
            dto.setDescription("x");
            roleService.createRole(dto);
        }

        PageInfo<RoleVO> page = roleService.listRoles(1, 10, "list_role_");
        assertNotNull(page);
        assertTrue(page.getTotal() >= 3);
        assertFalse(page.getList().isEmpty());
        assertTrue(page.getList().stream().allMatch(r -> r.getRoleName().contains("list_role_")));
    }
}

