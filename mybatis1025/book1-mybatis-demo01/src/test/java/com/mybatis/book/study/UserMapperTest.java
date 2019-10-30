package com.mybatis.book.study;

import com.mybatis.book.study.mapper.UserMapper;
import com.mybatis.book.study.model.SysRole;
import com.mybatis.book.study.model.SysRoleExtend;
import com.mybatis.book.study.model.SysUser;
import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

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

            assertEquals(1, userMapper.deleteById(1L));
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


    @Test
    public void testSelectByUser() {
        SqlSession sqlSession = getSqlSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            SysUser query = new SysUser();
            query.setUserName("ad");

            List<SysUser> userList = userMapper.selectByUser(query);
            assertTrue(userList.size() > 0);

            query = new SysUser();
            query.setUserEmail("test@mybatis.tk");
            userList = userMapper.selectByUser(query);
            assertTrue(userList.size() > 0);

            query = new SysUser();
            query.setUserName("ad");
            query.setUserEmail("test@mybatis.tk");

            userList = userMapper.selectByUser(query);
            assertTrue(userList.size() == 0);

        } finally {
            sqlSession.close();
        }
    }


    @Test
    public void testUpdateByIdSelective() {
        SqlSession sqlSession = getSqlSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            SysUser user = new SysUser();
            user.setId(1L);

            user.setUserEmail("test@mybatis.tk");
            int result = userMapper.updateByIdSelective(user);
            assertEquals(1, result);

            user = userMapper.selectById(1L);

            assertEquals("admin", user.getUserName());
            assertEquals("test@mybatis.tk", user.getUserEmail());

        } finally {
            sqlSession.rollback();
            sqlSession.close();
        }
    }


    @Test
    public void testInsertSelective() {
        SqlSession sqlSession = getSqlSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

            SysUser user = new SysUser();
            user.setUserName("test-selective");
            user.setUserPassword("123456");
            user.setUserInfo("test info");
            user.setUserEmail("test@mybatis.tk");
            user.setCreateTime(new Date());

            userMapper.insertSelective(user);
            user = userMapper.selectById(user.getId());
            assertEquals("test@mybatis.tk", user.getUserEmail());

        } finally {
            sqlSession.rollback();
            sqlSession.close();
        }
    }

    @Test
    public void testSelectByIdOrUserName() {
        SqlSession sqlSession = getSqlSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

            SysUser query = new SysUser();
            query.setId(1L);
            query.setUserName("admin");

            SysUser user = userMapper.selectByIdOrUserName(query);
            assertNotNull(user);

            query.setId(null);
            user = userMapper.selectByIdOrUserName(query);
            assertNotNull(user);

            query.setUserName(null);
            user = userMapper.selectByIdOrUserName(query);
            assertNull(user);

        } finally {
            sqlSession.rollback();
            sqlSession.close();
        }
    }

    @Test
    public void testSelectByIdList() {
        SqlSession sqlSession = getSqlSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            List<Long> idList = new ArrayList<>();

            idList.add(1L);
            idList.add(1001L);

            List<SysUser> userList = userMapper.selectByIdList(idList);
            assertEquals(2, userList.size());

        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testInsertList() {
        SqlSession sqlSession = getSqlSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            List<SysUser> userList = new ArrayList<>();
            for (int i = 0; i < 2; i++) {
                SysUser user = new SysUser();

                user.setUserName("test"+i);
                user.setUserPassword("123456");
                user.setUserEmail("test@mybatis.tk");
                userList.add(user);
            }
            int result = userMapper.insertList(userList);
            assertEquals(2,result);
            for (SysUser user : userList) {
                System.out.println(user.getId());
            }
        } finally {
            sqlSession.rollback();
            sqlSession.close();
        }
    }

    @Test
    public void testUpldateByMap(){
        SqlSession sqlSession = getSqlSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

            Map<String,Object> map = new HashMap<>();
            map.put("id",1L);
            map.put("user_email","test@mybatis.tk");
            map.put("user_password","123456");

            userMapper.updateByMap(map);

            SysUser user = userMapper.selectById(1L);
            assertEquals("test@mybatis.tk",user.getUserEmail());
        }finally {
            sqlSession.rollback();
            sqlSession.close();

        }
    }


}
