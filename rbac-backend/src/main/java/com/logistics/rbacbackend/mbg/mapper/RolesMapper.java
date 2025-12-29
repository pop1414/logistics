package com.logistics.rbacbackend.mbg.mapper;

import com.logistics.rbacbackend.mbg.model.Roles;
import com.logistics.rbacbackend.mbg.model.RolesExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RolesMapper {
    long countByExample(RolesExample example);

    int deleteByExample(RolesExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Roles row);

    int insertSelective(Roles row);

    List<Roles> selectByExample(RolesExample example);

    Roles selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") Roles row, @Param("example") RolesExample example);

    int updateByExample(@Param("row") Roles row, @Param("example") RolesExample example);

    int updateByPrimaryKeySelective(Roles row);

    int updateByPrimaryKey(Roles row);
}