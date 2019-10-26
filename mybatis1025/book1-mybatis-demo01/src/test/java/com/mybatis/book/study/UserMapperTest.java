package com.mybatis.book.study;

import com.mybatis.book.study.mapper.UserMapper;
import com.mybatis.book.study.model.SysRole;
import com.mybatis.book.study.model.SysRoleExtend;
import com.mybatis.book.study.model.SysUser;
import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

public class UserMapperTest extends BaseMapperTest {


    @Test
    public void testSelectById() {
        SqlSession sqlSession = getSqlSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            SysUser sysUser = userMapper.selectById(1L);
            assertNotNull(sysUser);
            assertEquals("admin", sysUser.getUserName());

        } finally {
            sqlSession.close();
        }
    }


    @Test
    public void testSelectAll() {
        SqlSession sqlSession = getSqlSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            List<SysUser> userList = userMapper.selectAll();
            assertNotNull(userList);
            assertTrue(userList.size() > 0);
        } finally {
            sqlSession.close();
        }
    }


    @Test
    public void testSelectRolesByUserId() {
        SqlSession sqlSession = getSqlSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            List<SysRole> roles = userMapper.selectRolesByUserId(1L);
            assertNotNull(roles);
            assertTrue(roles.size() > 0);
        } finally {
            sqlSession.close();
        }
    }


    @Test
    public void testSelectRolesByUserIdExtend() {
        SqlSession sqlSession = getSqlSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            List<SysRoleExtend> roles = userMapper.selectRolesByUserIdExtend(1L);
            assertNotNull(roles);
            assertTrue(roles.size() > 0);
        } finally {
            sqlSession.close();
        }
    }


    @Test
    public void testSelectRolesByUserIdWithSysUser() {
        SqlSession sqlSession = getSqlSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            List<SysRole> roles = userMapper.selectRolesByUserIdWithSysUser(1L);
            assertNotNull(roles);
            assertTrue(roles.size() > 0);
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testInsert() {

        SqlSession sqlSession = getSqlSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            SysUser user = new SysUser();
            user.setUserName("test1");
            user.setUserPassword("123456");
            user.setUserEmail("test@mybatis.tk");
            user.setUserInfo("test info");
            user.setHeadImg(new byte[]{1, 2, 3});
            user.setCreateTime(new Date());

            int result = userMapper.insert(user);
            assertEquals(result, 1);
            // 这种情况下，未返回id
            assertNull(user.getId());

        } finally {
            sqlSession.close();
        }
    }


    @Test
    public void testInsert2() {

        SqlSession sqlSession = getSqlSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            SysUser user = new SysUser();
            user.setUserName("test1");
            user.setUserPassword("123456");
            user.setUserEmail("test@mybatis.tk");
            user.setUserInfo("test info");
            user.setHeadImg(new byte[]{1, 2, 3});
            user.setCreateTime(new Date());

            int result = userMapper.insert2(user);
            assertEquals(result, 1);
            // 返回id
            assertNotNull(user.getId());

        } finally {
            sqlSession.rollback();
            sqlSession.close();
        }
    }


    @Test
    public void testInsert3() {

        SqlSession sqlSession = getSqlSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            SysUser user = new SysUser();
            user.setUserName("test1");
            user.setUserPassword("123456");
            user.setUserEmail("test@mybatis.tk");
            user.setUserInfo("test info");
            user.setHeadImg(new byte[]{1, 2, 3});
            user.setCreateTime(new Date());

            int result = userMapper.insert3(user);
            assertEquals(result, 1);
            // 返回id
            assertNotNull(user.getId());

        } finally {
            sqlSession.rollback();
            sqlSession.close();
        }
    }


    @Test
    public void testUpdateById() {

        SqlSession sqlSession = getSqlSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

            SysUser user = userMapper.selectById(1L);
            assertEquals("admin", user.getUserName());
            user.setUserName("admin_test");
            user.setUserEmail("test@mybatis.tk");

            int result = userMapper.updateById(user);
            assertEquals(result, 1);
            user = userMapper.selectById(1L);
            assertEquals("admin_test", user.getUserName());
        } finally {
            sqlSession.rollback();
            sqlSession.close();
        }
    }


    @Test
    public void testDeleteById() {

        SqlSession sqlSession = getSqlSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

            SysUser user1 = userMapper.selectById(1L);
            assertNotNull(user1);

            assertEquals(1,userMapper.deleteById(1L));
            assertNull(userMapper.selectById(1L));


        } finally {
            sqlSession.rollback();
            sqlSession.close();
        }
    }


    @Test
    public void testSelectRolesByUserIdAndRoleEnabled() {

        SqlSession sqlSession = getSqlSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

            List<SysRole> roleList = userMapper.selectRolesByUserIdAndRoleEnabled(1L, 1);
            assertNotNull(roleList);
            assertTrue(roleList.size() > 0);

        } finally {
            sqlSession.close();
        }
    }


    @Test
    public void testSelectRolesByUserAndRole() {

        SqlSession sqlSession = getSqlSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            SysUser user = new SysUser();
            user.setId(1L);
            SysRole sysRole = new SysRole();
            sysRole.setEnabled(1);
            List<SysRole> roleList = userMapper.selectRolesByUserAndRole(user, sysRole);
            assertNotNull(roleList);
            assertTrue(roleList.size() > 0);

        } finally {
            sqlSession.close();
        }
    }


}
