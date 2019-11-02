package com.mybatis.book.study;

import com.mybatis.book.study.mapper.RoleMapper;
import com.mybatis.book.study.mapper.UserMapper;
import com.mybatis.book.study.model.SysRole;
import com.mybatis.book.study.model.SysUser;
import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class CacheTest extends BaseMapperTest {


    @Test
    public void testL1Cache() {

        SqlSession sqlSession = getSqlSession();
        SysUser user1 = null;
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

            user1 = userMapper.selectById(1L);
            user1.setUserName("New Name");
            // 没有发送sql查询
            SysUser user2 = userMapper.selectById(1L);
            System.out.println("user1Name=" + user1.getUserName() + ",user2Name=" + user2.getUserName());
            //assertEquals("New Name", user2.getUserName());
            //assertEquals(user1, user2);

        } finally {
            sqlSession.close();
        }

        System.out.println("开启新的sqlSession");

        sqlSession = getSqlSession();

        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            SysUser user2 = userMapper.selectById(1L);
            System.out.println("user2Name=" + user2.getUserName());
//            assertEquals("New Name", user2.getUserName());
//            assertEquals(user1, user2);

            userMapper.deleteById(2L);

            SysUser user3 = userMapper.selectById(1L);
            assertNotEquals(user2, user3);


        } finally {
            sqlSession.close();
        }


    }


    @Test
    public void testL2Cache() {
        SqlSession sqlSession = getSqlSession();

        SysRole role1 = null;
        try {
            RoleMapper roleMapper = sqlSession.getMapper(RoleMapper.class);

            role1 = roleMapper.selectById(1L);
            role1.setRoleName("New Name");

            SysRole role2 = roleMapper.selectById(1L);
            System.out.println("role2Name=" + role2.getRoleName());

            // 这里role1和role2是由于sqlSession缓存查询的结果，是同一个实例
            assertEquals(role1,role2);

        } finally {
            sqlSession.close();
        }


        System.out.println("开启新的SqlSession");
        sqlSession = getSqlSession();
        try {
            RoleMapper roleMapper = sqlSession.getMapper(RoleMapper.class);

            SysRole role2 = roleMapper.selectById(1L);
            System.out.println("role2Name=" + role2.getRoleName());
            // 这里role1是上一个sqlSession结果，role2是二级缓存的结果，所以连个不是同一个实例(这里感觉不太对啊)
            assertNotEquals(role1,role2);
            SysRole role3 = roleMapper.selectById(1L);
            // role2和role3 是两个实例(这里感觉不太对啊)
            assertNotEquals(role2,role3);
        } finally {
            sqlSession.close();
        }

    }


}
