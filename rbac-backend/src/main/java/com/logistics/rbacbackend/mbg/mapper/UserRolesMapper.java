package com.logistics.rbacbackend.mbg.mapper;

import com.logistics.rbacbackend.mbg.model.UserRoles;
import com.logistics.rbacbackend.mbg.model.UserRolesExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserRolesMapper {
    long countByExample(UserRolesExample example);

    int deleteByExample(UserRolesExample example);

    int deleteByPrimaryKey(@Param("userId") Long userId, @Param("roleId") Long roleId);

    int insert(UserRoles row);

    int insertSelective(UserRoles row);

    List<UserRoles> selectByExample(UserRolesExample example);

    int updateByExampleSelective(@Param("row") UserRoles row, @Param("example") UserRolesExample example);

    int updateByExample(@Param("row") UserRoles row, @Param("example") UserRolesExample example);
}