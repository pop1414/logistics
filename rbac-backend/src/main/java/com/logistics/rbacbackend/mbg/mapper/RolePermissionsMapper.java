package com.logistics.rbacbackend.mbg.mapper;

import com.logistics.rbacbackend.mbg.model.RolePermissions;
import com.logistics.rbacbackend.mbg.model.RolePermissionsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RolePermissionsMapper {
    long countByExample(RolePermissionsExample example);

    int deleteByExample(RolePermissionsExample example);

    int deleteByPrimaryKey(@Param("roleId") Long roleId, @Param("permId") Long permId);

    int insert(RolePermissions row);

    int insertSelective(RolePermissions row);

    List<RolePermissions> selectByExample(RolePermissionsExample example);

    int updateByExampleSelective(@Param("row") RolePermissions row, @Param("example") RolePermissionsExample example);

    int updateByExample(@Param("row") RolePermissions row, @Param("example") RolePermissionsExample example);
}