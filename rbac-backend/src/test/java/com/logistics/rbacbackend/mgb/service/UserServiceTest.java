package com.logistics.rbacbackend.mgb.service;

import com.github.pagehelper.PageInfo;
import com.logistics.rbacbackend.dto.UserCreateDTO;
import com.logistics.rbacbackend.dto.UserUpdateDTO;
import com.logistics.rbacbackend.dto.UserUpdatePasswordDTO;
import com.logistics.rbacbackend.mbg.mapper.RolesMapper;
import com.logistics.rbacbackend.mbg.mapper.UserRolesMapper;
import com.logistics.rbacbackend.mbg.mapper.UsersMapper;
import com.logistics.rbacbackend.mbg.model.Roles;
import com.logistics.rbacbackend.mbg.model.UserRoles;
import com.logistics.rbacbackend.mbg.model.Users;
import com.logistics.rbacbackend.service.UserService;
import com.logistics.rbacbackend.vo.UserVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author DDY
 * @version 1.0
 * @date 2025/12/30-11:01
 * @description com.logistics.rbacbackend.mgb.service
 */
@SpringBootTest
@Transactional
@Rollback
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RolesMapper rolesMapper;

    @Autowired
    private UserRolesMapper userRolesMapper;

    private String uniq(String prefix) {
        return prefix + System.currentTimeMillis();
    }

    @Test
    void createUser_shouldInsert_andPasswordIsEncoded() {
        UserCreateDTO dto = new UserCreateDTO();
        dto.setUsername(uniq("alice_"));
        dto.setPassword("123456");
        dto.setEmail("a@a.com");

        Long userId = userService.createUser(dto);
        assertNotNull(userId);

        Users db = usersMapper.selectByPrimaryKey(userId);
        assertNotNull(db);
        assertEquals(dto.getUsername(), db.getUsername());
        assertEquals(dto.getEmail(), db.getEmail());
        // 密码应为加密后的哈希，且能匹配明文
        assertTrue(passwordEncoder.matches("123456", db.getPassword()));
    }

    @Test
    void createUser_duplicateUsername_shouldThrow() {
        String username = uniq("dup_");

        UserCreateDTO dto1 = new UserCreateDTO();
        dto1.setUsername(username);
        dto1.setPassword("123456");
        dto1.setEmail("x@x.com");
        userService.createUser(dto1);

        UserCreateDTO dto2 = new UserCreateDTO();
        dto2.setUsername(username);
        dto2.setPassword("123456");
        dto2.setEmail("y@y.com");

        assertThrows(IllegalArgumentException.class, () -> userService.createUser(dto2));
    }

    @Test
    void updateUser_shouldUpdateEmail() {
        UserCreateDTO dto = new UserCreateDTO();
        dto.setUsername(uniq("u_"));
        dto.setPassword("123456");
        dto.setEmail("old@a.com");

        Long userId = userService.createUser(dto);

        UserUpdateDTO upd = new UserUpdateDTO();
        upd.setEmail("new@a.com");
        userService.updateUser(userId, upd);

        Users db = usersMapper.selectByPrimaryKey(userId);
        assertEquals("new@a.com", db.getEmail());
    }

    @Test
    void updatePassword_shouldChangePassword_whenOldPasswordCorrect() {
        UserCreateDTO dto = new UserCreateDTO();
        dto.setUsername(uniq("pwd_"));
        dto.setPassword("oldpass");
        dto.setEmail("p@p.com");

        Long userId = userService.createUser(dto);

        UserUpdatePasswordDTO pwd = new UserUpdatePasswordDTO();
        pwd.setOldPassword("oldpass");
        pwd.setNewPassword("newpass");
        userService.updatePassword(userId, pwd);

        Users db = usersMapper.selectByPrimaryKey(userId);
        assertTrue(passwordEncoder.matches("newpass", db.getPassword()));
        assertFalse(passwordEncoder.matches("oldpass", db.getPassword()));
    }

    @Test
    void updatePassword_shouldThrow_whenOldPasswordWrong() {
        UserCreateDTO dto = new UserCreateDTO();
        dto.setUsername(uniq("pwd2_"));
        dto.setPassword("oldpass");
        dto.setEmail("p2@p.com");

        Long userId = userService.createUser(dto);

        UserUpdatePasswordDTO pwd = new UserUpdatePasswordDTO();
        pwd.setOldPassword("WRONG");
        pwd.setNewPassword("newpass");

        assertThrows(IllegalArgumentException.class, () -> userService.updatePassword(userId, pwd));
    }

    @Test
    void listUsers_shouldReturnPaged_andSupportLikeQuery() {
        // 插入 3 个带前缀的用户
        for (int i = 0; i < 3; i++) {
            UserCreateDTO dto = new UserCreateDTO();
            dto.setUsername(uniq("list_") + "_" + i);
            dto.setPassword("123456");
            dto.setEmail("l@l.com");
            userService.createUser(dto);
        }

        PageInfo<UserVO> page = userService.listUsers(1, 10, "list_");
        assertNotNull(page);
        assertTrue(page.getTotal() >= 3);
        assertFalse(page.getList().isEmpty());
        assertTrue(page.getList().stream().allMatch(u -> u.getUsername().contains("list_")));
    }

    @Test
    void getUserDetail_shouldReturnRoleIds_whenUserHasRoles() {
        // 1) 创建用户
        UserCreateDTO dto = new UserCreateDTO();
        dto.setUsername(uniq("detail_"));
        dto.setPassword("123456");
        dto.setEmail("d@d.com");
        Long userId = userService.createUser(dto);

        // 2) 创建角色
        Roles role = new Roles();
        role.setRoleName(uniq("role_"));
        role.setDescription("test role");
        role.setIsInitial(false); // 你若是 tinyint(1)，可能是 0/1，按你表字段类型改
        rolesMapper.insert(role);
        assertNotNull(role.getId());

        // 3) 手动插 user_roles 关联（因为 assignRoles 属于 RbacService）
        UserRoles ur = new UserRoles();
        ur.setUserId(userId);
        ur.setRoleId(role.getId());
        userRolesMapper.insert(ur);

        // 4) 查详情
        UserVO vo = userService.getUserDetail(userId);
        assertNotNull(vo);
        assertEquals(userId, vo.getId());
        assertNotNull(vo.getRoleIds());
        assertTrue(vo.getRoleIds().contains(role.getId()));
    }
}
