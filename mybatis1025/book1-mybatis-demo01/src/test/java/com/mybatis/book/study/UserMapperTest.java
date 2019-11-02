package com.mybatis.book.study;

import com.mybatis.book.study.mapper.UserMapper;
import com.mybatis.book.study.model.SysPrivilege;
import com.mybatis.book.study.model.SysRole;
import com.mybatis.book.study.model.SysRoleExtend;
import com.mybatis.book.study.model.SysUser;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
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
//            sysRole.setEnabled(1);
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

                user.setUserName("test" + i);
                user.setUserPassword("123456");
                user.setUserEmail("test@mybatis.tk");
                userList.add(user);
            }
            int result = userMapper.insertList(userList);
            assertEquals(2, result);
            for (SysUser user : userList) {
                System.out.println(user.getId());
            }
        } finally {
            sqlSession.rollback();
            sqlSession.close();
        }
    }

    @Test
    public void testUpldateByMap() {
        SqlSession sqlSession = getSqlSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

            Map<String, Object> map = new HashMap<>();
            map.put("id", 1L);
            map.put("user_email", "test@mybatis.tk");
            map.put("user_password", "123456");

            userMapper.updateByMap(map);

            SysUser user = userMapper.selectById(1L);
            assertEquals("test@mybatis.tk", user.getUserEmail());
        } finally {
            sqlSession.rollback();
            sqlSession.close();

        }
    }

    @Test
    public void testSelectUserAndRoleById() {
        SqlSession sqlSession = getSqlSession();

        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            SysUser user = userMapper.selectUserAndRoleById(1001L);

            assertNotNull(user);
            assertNotNull(user.getRole());
        } finally {
            sqlSession.close();
        }
    }

    @Test
    public void testSelectUserAndRoleById2() {
        SqlSession sqlSession = getSqlSession();

        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            SysUser user = userMapper.selectUserAndRoleById2(1001L);

            assertNotNull(user);
            assertNotNull(user.getRole());
        } finally {
            sqlSession.close();
        }
    }


    @Test
    public void testSelectUserAndRoleById3() {
        SqlSession sqlSession = getSqlSession();

        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            SysUser user = userMapper.selectUserAndRoleById3(1001L);

            assertNotNull(user);
            assertNotNull(user.getRole());
        } finally {
            sqlSession.close();
        }
    }


    @Test
    public void testSelectUserAndRoleByIdSelect() {
        SqlSession sqlSession = getSqlSession();

        try {
            // 查询了两次，分别先是userMapper.selectById 后是roleMapper.selectRoleById
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            SysUser user = userMapper.selectUserAndRoleByIdSelect(1001L);

            /**
             * 当在mybatis-config.xml中配置拦截在时，<setting name="aggressiveLazyLoading" value="false"/> ，
             * 且resultMap的association标签配置fetchType="Lazy"时，会使用懒加载，即user.getRole()时，才会发送sql去查询role信息
             *
             */
//            assertNotNull(user);
            /**
             * 懒加载配置的情况下：调用equals,clone,hashCode,toString方法，也会触发积极加载，
             * 通过lazyLoadTriggerMethods可以配置方法。
             */
            System.out.println(user.toString());
//            assertNotNull(user.getRole());
        } finally {
            sqlSession.close();
        }
    }


    @Test
    public void testSelectAllUserAndRoles() {
        SqlSession sqlSession = getSqlSession();

        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            List<SysUser> userList = userMapper.selectAllUserAndRoles();

            System.out.println("用户数：" + userList.size());

            for (SysUser user : userList) {
                System.out.println("用户名：" + user.getUserName());
                for (SysRole sysRole : user.getRoleList()) {
                    System.out.println("角色名：" + sysRole.getRoleName());
                }
            }
            /**
             * DEBUG [main] - ==>  Preparing: select u.id, u.user_name , u.user_password , u.user_email , u.user_info , u.head_img , u.create_time , r.id role_id, r.role_name role_role_name, r.enabled role_enabled, r.create_by role_create_by, r.create_time role_create_time from sys_user u inner join sys_user_role ur on u.id = ur.user_id inner join sys_role r on ur.role_id = r.id
             * DEBUG [main] - ==> Parameters:
             * TRACE [main] - <==    Columns: id, user_name, user_password, user_email, user_info, head_img, create_time, role_id, role_role_name, role_enabled, role_create_by, role_create_time
             * TRACE [main] - <==        Row: 1, admin, 123456, admin@mybatis.tk, <<BLOB>>, <<BLOB>>, 2019-10-25 16:00:00.0, 1, 管理员, 1, 1, 2019-10-25 16:00:00.0
             * TRACE [main] - <==        Row: 1, admin, 123456, admin@mybatis.tk, <<BLOB>>, <<BLOB>>, 2019-10-25 16:00:00.0, 2, 普通用户, 1, 1, 2019-10-25 16:00:00.0
             * TRACE [main] - <==        Row: 1001, test, 123456, test@mybatis.tk, <<BLOB>>, <<BLOB>>, 2019-10-25 16:00:00.0, 2, 普通用户, 1, 1, 2019-10-25 16:00:00.0
             * DEBUG [main] - <==      Total: 3
             * 用户数：2
             * 用户名：admin
             * 角色名：管理员
             * 角色名：普通用户
             * 用户名：test
             * 角色名：普通用户
             *
             *
             * 查询出来  Total: 3 是3条，但是 输出结果是2条，原因是mybatis会根据主键进行合并操作，主键相同的则进行合并
             *
             */

        } finally {
            sqlSession.close();
        }
    }


    @Test
    public void testSelectAllUserAndRolesAndPrivileges() {
        SqlSession sqlSession = getSqlSession();

        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            List<SysUser> userList = userMapper.selectAllUserAndRolesAndPrivileges();

            System.out.println("用户数：" + userList.size());
            for (SysUser user : userList) {
                System.out.println("用户名：" + user.getUserName());
                for (SysRole sysRole : user.getRoleList()) {
                    System.out.println("角色名：" + sysRole.getRoleName());
                    for (SysPrivilege sysPrivilege : sysRole.getPrivilegeList()) {
                        System.out.println("权限名：" + sysPrivilege.getPrivilegeName());
                    }
                }
            }
        } finally {
            sqlSession.close();
        }
    }


    @Test
    public void testSelectAllUserAndRolesSelect() {
        SqlSession sqlSession = getSqlSession();
        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            SysUser user = userMapper.selectAllUserAndRolesSelect(1L);

            System.out.println("用户名：" + user.getUserName());
            for (SysRole role : user.getRoleList()) {
                System.out.println("角色名：" + role.getRoleName());
                for (SysPrivilege privilege : role.getPrivilegeList()) {
                    System.out.println("权限名：" + privilege.getPrivilegeName());
                }
            }
        } finally {
            sqlSession.close();
        }
    }


    @Test
    public void testSelectUserById() {
        SqlSession sqlSession = getSqlSession();

        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            SysUser user = new SysUser();
            user.setId(1L);
            userMapper.selectUserById(user);

            assertNotNull(user.getUserName());
            System.out.println("用户名：" + user.getUserName());
        } finally {
            sqlSession.close();
        }
    }


    @Test
    public void testSelectUserPage() {
        SqlSession sqlSession = getSqlSession();

        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
            Map<String, Object> params = new HashMap<>();
            params.put("userName", "ad");
            params.put("offset", 0);
            params.put("limit", 10);

            List<SysUser> userList = userMapper.selectUserPage(params);
            Long total = (Long) params.get("total");

            System.out.println("总数：" + total);
            for (SysUser user : userList) {
                System.out.println("用户名：" + user.getUserName());
            }
        } finally {
            sqlSession.close();
        }
    }


    @Test
    public void testInsertAndDelete() {
        SqlSession sqlSession = getSqlSession();

        try {
            UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

            SysUser user = new SysUser();
            user.setUserName("test1");
            user.setUserPassword("123456");
            user.setUserEmail("test@mybatis.tk");
            user.setUserInfo("test info");

            user.setHeadImg(new byte[]{1, 2, 3});

            userMapper.insertUserAndRoles(user, "1,2");
            assertNotNull(user.getId());
            assertNotNull(user.getCreateTime());

//            sqlSession.commit();
            // 测试删除刚刚插入的数据
            userMapper.deleteById(user.getId());

        } finally {
            sqlSession.close();
        }
    }

}
