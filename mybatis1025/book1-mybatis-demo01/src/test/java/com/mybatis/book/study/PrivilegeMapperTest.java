package com.mybatis.book.study;

import com.mybatis.book.study.mapper.PrivilegeMapper;
import com.mybatis.book.study.model.SysPrivilege;
import org.apache.ibatis.session.SqlSession;
import org.junit.Assert;
import org.junit.Test;

public class PrivilegeMapperTest extends BaseMapperTest {


    @Test
    public void testSelectById() {
        SqlSession sqlSession = getSqlSession();
        try {
            PrivilegeMapper privilegeMapper = sqlSession.getMapper(PrivilegeMapper.class);
            SysPrivilege privilege = privilegeMapper.selectById(1L);
            Assert.assertNotNull(privilege);
            Assert.assertEquals("用户管理", privilege.getPrivilegeName());

        } finally {
            sqlSession.close();
        }


    }

}
