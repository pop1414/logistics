package com.logistics.rbacbackend.service;


import com.logistics.rbacbackend.dao.RbacQueryDao;
import com.logistics.rbacbackend.mbg.mapper.PermissionsMapper;
import com.logistics.rbacbackend.mbg.mapper.RolesMapper;
import com.logistics.rbacbackend.mbg.mapper.UsersMapper;
import com.logistics.rbacbackend.mbg.model.Permissions;
import com.logistics.rbacbackend.mbg.model.Roles;
import com.logistics.rbacbackend.mbg.model.Users;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author DDY
 * @version 1.0
 * @date 2025/12/30-13:33
 */
@SpringBootTest
@Transactional
class RbacIntegrationTest {

    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private RolesMapper rolesMapper;
    @Autowired
    private PermissionsMapper permissionsMapper;

    @Autowired
    private RbacService rbacService;
    @Autowired
    private RbacQueryDao rbacQueryDao;

    @Test
    void rbac_full_chain_insert_assign_query_should_work() {
        String suffix = String.valueOf(System.currentTimeMillis());

        // 1) 插入 role
        Roles role = new Roles();
        role.setRoleName("test_role_" + suffix);
        role.setDescription("role for integration test");
        rolesMapper.insertSelective(role);
        assertNotNull(role.getId());
        Long roleId = role.getId();

        // 2) 插入 permissions
        Permissions p1 = new Permissions();
        p1.setPermName("test:read_" + suffix);
        p1.setDescription("read for test");
        p1.setModule("test");
        permissionsMapper.insertSelective(p1);
        assertNotNull(p1.getId());

        Permissions p2 = new Permissions();
        p2.setPermName("test:write_" + suffix);
        p2.setDescription("write for test");
        p2.setModule("test");
        permissionsMapper.insertSelective(p2);
        assertNotNull(p2.getId());

        List<Long> permIds = List.of(p1.getId(), p2.getId());

        // 3) role <- perms
        rbacService.assignPermsToRole(roleId, permIds);

        // 4) 插入 user
        Users u = new Users();
        u.setUsername("u_" + suffix);
        u.setPassword("plain_for_test"); // 不测登录，不必 bcrypt
        u.setEmail("u_" + suffix + "@test.com");
        usersMapper.insertSelective(u);
        assertNotNull(u.getId());
        Long userId = u.getId();

        // 5) user <- roles
        rbacService.assignRolesToUser(userId, List.of(roleId));

        // 6) assert：user -> roleIds
        List<Long> roleIds = rbacQueryDao.selectRoleIdsByUserId(userId);
        assertNotNull(roleIds);
        assertTrue(roleIds.contains(roleId));

        // 7) assert：role -> permIds
        List<Long> permIdList = rbacQueryDao.selectPermIdsByRoleId(roleId);
        assertNotNull(permIdList);
        assertEquals(new HashSet<>(permIds), new HashSet<>(permIdList));

        // 8) assert：user -> permCodes
        List<String> codes = rbacQueryDao.selectPermCodesByUserId(userId);
        assertNotNull(codes);
        assertTrue(codes.contains(p1.getPermName()));
        assertTrue(codes.contains(p2.getPermName()));
    }

    @Test
    void assign_empty_should_clear_relations() {
        String suffix = String.valueOf(System.currentTimeMillis());

        // role
        Roles role = new Roles();
        role.setRoleName("test_role_clear_" + suffix);
        rolesMapper.insertSelective(role);
        Long roleId = role.getId();

        // perm
        Permissions p = new Permissions();
        p.setPermName("test:clear_" + suffix);
        p.setModule("test");
        permissionsMapper.insertSelective(p);
        Long permId = p.getId();

        // user
        Users u = new Users();
        u.setUsername("u_clear_" + suffix);
        u.setPassword("x");
        usersMapper.insertSelective(u);
        Long userId = u.getId();

        // assign then clear
        rbacService.assignPermsToRole(roleId, List.of(permId));
        rbacService.assignRolesToUser(userId, List.of(roleId));

        assertFalse(rbacQueryDao.selectPermCodesByUserId(userId).isEmpty());

        rbacService.assignRolesToUser(userId, List.of()); // 清空角色
        assertTrue(rbacQueryDao.selectRoleIdsByUserId(userId).isEmpty());
        assertTrue(rbacQueryDao.selectPermCodesByUserId(userId).isEmpty());

        // 清空 role perms
        rbacService.assignPermsToRole(roleId, List.of());
        assertTrue(rbacQueryDao.selectPermIdsByRoleId(roleId).isEmpty());
    }
}

