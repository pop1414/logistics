package com.logistics.rbacbackend.mgb.mapper;

import com.logistics.rbacbackend.mbg.mapper.UsersMapper;
import com.logistics.rbacbackend.mbg.model.Users;
import com.logistics.rbacbackend.mbg.model.UsersExample;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author DDY
 * @version 1.0
 * @date 2025/12/29-23:09
 */
@SpringBootTest
// @Transactional + @Rollback 会让插入的数据在测试结束后回滚，不污染数据库。
@Transactional
@Rollback
public class UserMapperTest {

    @Autowired
    private UsersMapper usersMapper;

    @Test
    public void testSelectByPrimaryKey() {
        Users u = new Users();
        u.setUsername("alice");
        u.setPassword("bcrypt_hash");
        u.setType("super_admin");

        int inserted = usersMapper.insert(u);
        assertEquals(1, inserted);

        assertNotNull(u.getId(), "insert 后 id 应该回填");

        UsersExample example = new UsersExample();
        example.createCriteria().andUsernameEqualTo(u.getUsername()).andTypeEqualTo(u.getType());

        List<Users> list = usersMapper.selectByExample(example);

        Users db = usersMapper.selectByPrimaryKey(u.getId());
        assertNotNull(db);
        assertEquals(u.getUsername(), db.getUsername());
    }
}
