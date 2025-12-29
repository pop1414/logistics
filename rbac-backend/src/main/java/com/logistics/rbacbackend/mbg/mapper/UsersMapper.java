package com.logistics.rbacbackend.mbg.mapper;

import com.logistics.rbacbackend.mbg.model.Users;
import com.logistics.rbacbackend.mbg.model.UsersExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UsersMapper {
    long countByExample(UsersExample example);

    int deleteByExample(UsersExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Users row);

    int insertSelective(Users row);

    List<Users> selectByExample(UsersExample example);

    Users selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("row") Users row, @Param("example") UsersExample example);

    int updateByExample(@Param("row") Users row, @Param("example") UsersExample example);

    int updateByPrimaryKeySelective(Users row);

    int updateByPrimaryKey(Users row);
}