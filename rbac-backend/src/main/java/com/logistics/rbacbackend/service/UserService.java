package com.logistics.rbacbackend.service;

import com.github.pagehelper.PageInfo;
import com.logistics.rbacbackend.dto.UserCreateDTO;
import com.logistics.rbacbackend.dto.UserUpdateDTO;
import com.logistics.rbacbackend.dto.UserUpdatePasswordDTO;
import com.logistics.rbacbackend.vo.UserVO;

/**
 * @author DDY
 * @version 1.0
 * @date 2025/12/29-23:57
 */
public interface UserService {
    /**
     * 创建用户（只写 users 表），返回 userId
     * 角色分配由上层（Controller）调用 RbacService 完成
     */
    Long createUser(UserCreateDTO dto);

    /**
     * 更新用户基础信息（如 email）
     */
    void updateUser(Long userId, UserUpdateDTO dto);

    /**
     * 修改密码（需要校验旧密码）
     */
    void updatePassword(Long userId, UserUpdatePasswordDTO dto);

    /**
     * 用户列表（分页，可按 username 模糊搜索）
     */
    PageInfo<UserVO> listUsers(int pageNum, int pageSize, String username);

    /**
     * 用户详情（包含 roleIds 回显）
     */
    UserVO getUserDetail(Long userId);

    /**
     * 删除用户（实训更推荐：你可以改成禁用字段；这里先物理删除示例）
     */
    void deleteUser(Long userId);
}
