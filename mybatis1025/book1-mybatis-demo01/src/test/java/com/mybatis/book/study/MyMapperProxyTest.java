package com.mybatis.book.study;

import com.mybatis.book.study.mapper.MyMapperProxy;
import com.mybatis.book.study.mapper.UserMapper;
import com.mybatis.book.study.model.SysUser;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.lang.reflect.Proxy;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class MyMapperProxyTest extends BaseMapperTest {


    @Test
    public void testMyMapperProxy(){
        SqlSession sqlSession = getSqlSession();

        MyMapperProxy<UserMapper> proxy = new MyMapperProxy<>(UserMapper.class, sqlSession);
        UserMapper userMapper =(UserMapper) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{UserMapper.class}, proxy);

        List<SysUser> userList = userMapper.selectAll();
        assertNotNull(userList);
        assertTrue(userList.size() > 0);

    }


}
