package com.logistics.rbacbackend.service.impl;

import com.logistics.rbacbackend.dao.RbacQueryDao;
import com.logistics.rbacbackend.dto.LoginDTO;
import com.logistics.rbacbackend.mbg.mapper.UsersMapper;
import com.logistics.rbacbackend.mbg.model.Users;
import com.logistics.rbacbackend.mbg.model.UsersExample;
import com.logistics.rbacbackend.service.AuthService;
import com.logistics.rbacbackend.utils.JwtTokenUtil;
import com.logistics.rbacbackend.vo.LoginVO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author DDY
 * @version 1.0
 * @date 2025/12/30-22:24
 * @description 用UsersMapper + PasswordEncoder
 */
@Service
public class AuthServiceImpl implements AuthService {
    private final UsersMapper usersMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final RbacQueryDao rbacQueryDao;

    public AuthServiceImpl(UsersMapper usersMapper,
                           PasswordEncoder passwordEncoder,
                           JwtTokenUtil jwtTokenUtil,
                           RbacQueryDao rbacQueryDao) {
        this.usersMapper = usersMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
        this.rbacQueryDao = rbacQueryDao;
    }

    @Override
    public LoginVO login(LoginDTO dto) {
        if (dto == null || dto.getUsername() == null || dto.getUsername().isBlank()) {
            throw new IllegalArgumentException("username 不能为空");
        }
        if (dto.getPassword() == null || dto.getPassword().isBlank()) {
            throw new IllegalArgumentException("password 不能为空");
        }

        UsersExample ex = new UsersExample();
        ex.createCriteria().andUsernameEqualTo(dto.getUsername().trim());
        List<Users> list = usersMapper.selectByExample(ex);
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("用户名或密码错误");
        }

        Users u = list.get(0);
        if (!passwordEncoder.matches(dto.getPassword(), u.getPassword())) {
            throw new IllegalArgumentException("用户名或密码错误");
        }

        String token = jwtTokenUtil.generateToken(u.getId(), u.getUsername());
        List<String> roleNames = rbacQueryDao.selectRoleNamesByUserId(u.getId());

        LoginVO vo = new LoginVO();
        vo.setToken(token);
        vo.setUserId(u.getId());
        vo.setUsername(u.getUsername());
        vo.setRoleNames(roleNames);
        return vo;
    }
}
