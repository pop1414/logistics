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
    /**
     * 新增角色
     *
     * @param dto 角色新增入参数据载体，封装前端传入的角色新增表单数据（角色名称、角色标识、备注、状态等）
     * @return java.lang.Long 新增成功的角色主键ID（数据库自增ID）
     */
    Long createRole(RoleCreateDTO dto);

    /**
     * 根据角色ID修改角色信息
     *
     * @param roleId 角色主键ID，必填，指定要修改的角色
     * @param dto    角色修改入参数据载体，封装前端传入的角色修改表单数据，仅传递需要修改的字段即可
     */
    void updateRole(Long roleId, RoleUpdateDTO dto);

    /**
     * 根据角色ID删除角色
     *
     * @param roleId 角色主键ID，必填
     * @note 该删除为【业务逻辑删除】（通用规范），不会物理删除数据库数据，仅修改角色的启用状态为禁用/删除状态；
     * 若为物理删除，会同步删除角色与用户、角色与菜单的关联关系数据
     */
    void deleteRole(Long roleId);

    /**
     * 角色列表分页查询（带角色名称关键词模糊搜索）
     * 核心查询接口，用于前端角色管理列表页展示，支持分页+条件检索
     *
     * @param pageNum         页码，从1开始（前端分页组件通用规则，非0开始）
     * @param pageSize        每页展示的数据条数，例如：10/20/30
     * @param roleNameKeyword 角色名称模糊搜索关键词，可为null/空字符串，为空时查询全部角色
     * @return com.github.pagehelper.PageInfo<RoleVO> 分页结果对象，封装分页信息+当前页的角色VO列表数据
     * PageInfo包含：总条数、总页数、当前页、每页条数、上一页/下一页标识等分页元数据
     */
    PageInfo<RoleVO> listRoles(int pageNum, int pageSize, String roleNameKeyword);

    /**
     * 根据角色ID查询角色详情信息
     * 用于前端角色详情页、角色编辑页回显完整的角色数据
     *
     * @param roleId 角色主键ID，必填
     * @return RoleVO 角色详情VO对象，封装角色的所有展示字段（主键、角色名、角色标识、备注、状态、创建时间、修改时间等）
     * @note 当传入的roleId不存在时，业务层会抛出【数据不存在】的业务异常，由全局异常处理器统一处理返回前端
     */
    RoleVO getRoleDetail(Long roleId);

    /**
     * 不分页查询全部角色列表（极简版）
     * 【专用接口】：给前端下拉选择框、单选框、角色分配弹窗等场景使用，无需分页、无需搜索条件
     *
     * @return java.util.List<RoleVO> 所有角色的VO列表，仅返回核心展示字段（ID+角色名称），排序规则：创建时间倒序/角色名称正序
     * @note 该接口默认返回【启用状态】的角色，过滤掉禁用/删除状态的角色，符合业务下拉选择的常规需求
     */
    // 可选：不分页版本（给下拉框用）
    List<RoleVO> listAllRoles();
}
