package com.mybatis.book.study;

import com.mybatis.book.study.mapper.RoleMapper;
import com.mybatis.book.study.model.SysPrivilege;
import com.mybatis.book.study.model.SysRole;
import com.sun.deploy.util.ArrayUtil;
import com.sun.tools.javac.util.ArrayUtils;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class RoleMapperTest extends BaseMapperTest {

    @Test
    public void testSelectById() {
        SqlSession sqlSession = getSqlSession();

        try {
            RoleMapper roleMapper = sqlSession.getMapper(RoleMapper.class);
            SysRole role = roleMapper.selectById(1L);
            assertNotNull(role);
            assertEquals("管理员", role.getRoleName());

        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testSelectById2() {
        SqlSession sqlSession = getSqlSession();
        // 测试驼峰命名映射
        try {
            RoleMapper roleMapper = sqlSession.getMapper(RoleMapper.class);
            SysRole role = roleMapper.selectById2(1L);
            assertNotNull(role);
            assertEquals("管理员", role.getRoleName());


        } finally {
            sqlSession.close();
        }
    }


    @Test
    public void testSelectAll() {
        SqlSession sqlSession = getSqlSession();
        try {
            RoleMapper roleMapper = sqlSession.getMapper(RoleMapper.class);
            List<SysRole> roles = roleMapper.selectAll();
            assertNotNull(roles);
            assertTrue(roles.size() > 0);
            assertNotNull(roles.get(0).getRoleName());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testInsert() {
        SqlSession sqlSession = getSqlSession();
        try {
            RoleMapper roleMapper = sqlSession.getMapper(RoleMapper.class);
            SysRole role = new SysRole();
            role.setRoleName("test");
            role.setEnabled(1);
            role.setCreateBy(1L);
            role.setCreateTime(new Date());
            int insert = roleMapper.insert(role);
            // 不反回主键
            System.out.println(role.getId());
            System.out.println(insert);
        } finally {
            sqlSession.rollback();
            sqlSession.close();
        }
    }


    @Test
    public void testInsert2() {
        SqlSession sqlSession = getSqlSession();
        try {
            RoleMapper roleMapper = sqlSession.getMapper(RoleMapper.class);
            SysRole role = new SysRole();
            role.setRoleName("test");
            role.setEnabled(1);
            role.setCreateBy(1L);
            role.setCreateTime(new Date());
            int insert = roleMapper.insert2(role);
            // 反回自增主键
            System.out.println(role.getId());
            System.out.println(insert);
        } finally {
            sqlSession.rollback();
            sqlSession.close();
        }
    }


    @Test
    public void testInsert3() {
        SqlSession sqlSession = getSqlSession();
        try {
            RoleMapper roleMapper = sqlSession.getMapper(RoleMapper.class);
            SysRole role = new SysRole();
            role.setRoleName("test");
            role.setEnabled(1);
            role.setCreateBy(1L);
            role.setCreateTime(new Date());
            int insert = roleMapper.insert3(role);
            // 反回非自增主键  这里可能不太恰当
            System.out.println(role.getId());
            System.out.println(insert);
        } finally {
            sqlSession.rollback();
            sqlSession.close();
        }
    }

    @Test
    public void testSelectAllRoleAndPrivileges() {
        SqlSession sqlSession = getSqlSession();
        try {
            RoleMapper roleMapper = sqlSession.getMapper(RoleMapper.class);
            List<SysRole> roles = roleMapper.selectAllRoleAndPrivileges();
            for (SysRole role : roles) {
                System.out.println("角色:" + role.getRoleName());
                for (SysPrivilege privilege : role.getPrivilegeList()) {
                    System.out.println("权限：" + privilege.getPrivilegeName());
                }
            }
        } finally {
            sqlSession.close();
        }
    }


}
