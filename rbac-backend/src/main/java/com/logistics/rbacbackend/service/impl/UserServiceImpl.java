package com.logistics.rbacbackend.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.logistics.rbacbackend.dao.RbacQueryDao;
import com.logistics.rbacbackend.dto.UserCreateDTO;
import com.logistics.rbacbackend.dto.UserUpdateDTO;
import com.logistics.rbacbackend.dto.UserUpdatePasswordDTO;
import com.logistics.rbacbackend.mbg.mapper.UsersMapper;
import com.logistics.rbacbackend.mbg.model.Users;
import com.logistics.rbacbackend.mbg.model.UsersExample;
import com.logistics.rbacbackend.service.UserService;
import com.logistics.rbacbackend.vo.UserVO;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * @author DDY
 * @version 1.0
 * @date 2025/12/29-23:56
 * @description com.logistics.rbacbackend.service
 */
@Service
public class UserServiceImpl implements UserService {
    private final UsersMapper usersMapper;
    private final RbacQueryDao rbacQueryMapper;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UsersMapper usersMapper,
                           RbacQueryDao rbacQueryMapper,
                           PasswordEncoder passwordEncoder) {
        this.usersMapper = usersMapper;
        this.rbacQueryMapper = rbacQueryMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public Long createUser(UserCreateDTO dto) {
        // TODO 1) 基本校验（更严格可以用 @Valid 在 Controller 做）
        if (dto == null || dto.getUsername() == null || dto.getUsername().isBlank()) {
            throw new IllegalArgumentException("username 不能为空");
        }
        if (dto.getPassword() == null || dto.getPassword().isBlank()) {
            throw new IllegalArgumentException("password 不能为空");
        }

        Users u = new Users();
        u.setUsername(dto.getUsername().trim());
        u.setEmail(dto.getEmail());
        // BCrypt
        u.setPassword(passwordEncoder.encode(dto.getPassword()));

        try {
            int rows = usersMapper.insert(u);
            if (rows != 1) {
                throw new IllegalStateException("创建用户失败");
            }
        } catch (DuplicateKeyException e) {
            // users.username unique
            throw new IllegalArgumentException("用户名已存在: " + dto.getUsername());
        }

        // MBG 一般会回填 id（useGeneratedKeys 或 selectKey）
        if (u.getId() == null) {
            // 如果没回填，需要检查 MBG generatedKey 或表主键配置
            throw new IllegalStateException("创建用户成功但未回填ID，请检查MBG generatedKey 配置");
        }
        return u.getId();
    }

    @Override
    @Transactional
    public void updateUser(Long userId, UserUpdateDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("参数不能为空");
        }

        if (userId == null) {
            throw new IllegalArgumentException("userId 不能为空");
        }
        Users exists = usersMapper.selectByPrimaryKey(userId);
        if (exists == null) {
            throw new IllegalArgumentException("用户不存在 userId=" + userId);
        }

        Users patch = new Users();
        patch.setId(userId);
        patch.setEmail(dto == null ? null : dto.getEmail());

        int rows = usersMapper.updateByPrimaryKeySelective(patch);
        if (rows != 1) {
            throw new IllegalArgumentException("更新用户失败: " + userId);
        }
    }

    @Override
    @Transactional
    public void updatePassword(Long userId, UserUpdatePasswordDTO dto) {
        if (userId == null) {
            throw new IllegalArgumentException("userId 不能为空");
        }
        if (dto == null) {
            throw new IllegalArgumentException("参数不能为空");
        }

        if ((dto.getOldPassword().isBlank()) || (dto.getNewPassword().isBlank())) {
            throw new IllegalArgumentException("旧密码/新密码不能为空");
        }

        Users u = usersMapper.selectByPrimaryKey(userId);
        if (u == null) {
            throw new IllegalArgumentException("用户不存在 userId=" + userId);
        }

        boolean ok = passwordEncoder.matches(dto.getOldPassword(), u.getPassword());
        if (!ok) {
            throw new IllegalArgumentException("旧密码不正确");
        }

        Users patch = new Users();
        patch.setId(userId);
        patch.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        int rows = usersMapper.updateByPrimaryKeySelective(patch);
        if (rows != 1) {
            throw new IllegalArgumentException("修改密码失败: " + userId);
        }
    }


    @Override
    public PageInfo<UserVO> listUsers(int pageNum, int pageSize, String username) {
        PageHelper.startPage(pageNum, pageSize);

        UsersExample example = new UsersExample();
        UsersExample.Criteria c = example.createCriteria();
        if (username != null && !username.isBlank()) {
            c.andUsernameLike("%" + username.trim() + "%");
        }
        example.setOrderByClause("id desc");

        List<Users> list = usersMapper.selectByExample(example);

        // 转 VO（列表页通常不需要 roleIds，省一次联表查询；你若要显示再查）
        List<UserVO> voList = list.stream().map(this::toVOWithoutRoles).toList();
        PageInfo<Users> page = new PageInfo<>(list);

        PageInfo<UserVO> out = new PageInfo<>();
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
    public UserVO getUserDetail(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId 不能为空");
        }

        Users u = usersMapper.selectByPrimaryKey(userId);
        if (u == null) {
            throw new IllegalArgumentException("用户不存在 userId=" + userId);
        }

        UserVO vo = toVOWithoutRoles(u);

        // 回显 roleIds（联表查询）
        List<Long> roleIds = rbacQueryMapper.selectRoleIdsByUserId(userId);
        vo.setRoleIds(roleIds == null ? Collections.emptyList() : roleIds);

        return vo;
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("userId 不能为空");
        }
        Users exist = usersMapper.selectByPrimaryKey(userId);
        if (exist == null) {
            throw new IllegalArgumentException("用户不存在: " + userId);
        }

        int rows = usersMapper.deleteByPrimaryKey(userId);
        if (rows != 1) {
            throw new IllegalArgumentException("删除用户失败: " + userId);
        }
    }

    private UserVO toVOWithoutRoles(Users u) {
        UserVO vo = new UserVO();
        vo.setId(u.getId());
        vo.setUsername(u.getUsername());
        vo.setEmail(u.getEmail());
        vo.setCreateTime(u.getCreateTime());
        vo.setUpdateTime(u.getUpdateTime());
        return vo;
    }

}
