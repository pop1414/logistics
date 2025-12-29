package com.logistics.rbacbackend.mbg.mapper;

import com.logistics.rbacbackend.mbg.model.Permissions;
import com.logistics.rbacbackend.mbg.model.PermissionsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PermissionsMapper {
    long countByExample(PermissionsExample example);

    int deleteByExample(PermissionsExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Permissions row);

    int insertSelective(Permissions row);

    List<Permissions> selectByExample(PermissionsExample example);

    Permissions selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") Permissions row, @Param("example") PermissionsExample example);

    int updateByExample(@Param("row") Permissions row, @Param("example") PermissionsExample example);

    int updateByPrimaryKeySelective(Permissions row);

    int updateByPrimaryKey(Permissions row);
}