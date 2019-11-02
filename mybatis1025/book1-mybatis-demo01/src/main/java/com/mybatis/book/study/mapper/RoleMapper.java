package com.mybatis.book.study.mapper;

import com.mybatis.book.study.model.SysRole;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.cache.decorators.FifoCache;

import java.util.List;


/**
 *
 *
 * readWrite和select标签中的readOnly属性一样
 * 这里true为读写，false为只读
 */
//@CacheNamespace(
//        eviction = FifoCache.class,
//        flushInterval = 60000,
//        size = 512,
//        readWrite = true
//)

/**
 *
 * 参照缓存
 */
@CacheNamespaceRef(RoleMapper.class)
public interface RoleMapper {

    /**
     * @param id
     * @return
     * @Select("select id, role_name, enabled, create_by, create_time from sys_role where id = #{id}")
     * 上述写法需要在mybatis-config.xml中配置mapUnderscoreToCamelCase，开启驼峰命名映射
     */
    @Select({"select id,role_name roleName,enabled,create_by createBy,create_time createTime from sys_role where id = #{id} "})
    SysRole selectById(Long id);


    /**
     * id属性时mybatis依赖在3.3.1版本之上才支持
     *
     * @param id
     * @return
     */
    @Results(id = "roleResultMap", value = {
            @Result(property = "id", column = "id", id = true),
            @Result(property = "roleName", column = "role_name"),
            @Result(property = "enabled", column = "enabled"),
            @Result(property = "createBy", column = "create_by"),
            @Result(property = "createTime", column = "create_time"),
    })
    @Select("select id, role_name, enabled, create_by, create_time from sys_role where id = #{id}")
    SysRole selectById2(Long id);


    /**
     * 引用selectById2方法上的roleResultMap
     *
     * @return
     */
    @ResultMap("roleResultMap")
    @Select("select * from sys_role")
    List<SysRole> selectAll();


    /**
     * 不需要返回主键
     *
     * @param sysRole
     * @return
     */
    @Insert("insert into sys_role (id,role_name,enabled,create_by,create_time) values (#{id},#{roleName},#{enabled},#{createBy},#{createTime,jdbcType=TIMESTAMP}    )")
    int insert(SysRole sysRole);


    /**
     * 返回自增id
     *
     * @param sysRole
     * @return
     */
    @Insert("insert into sys_role (role_name,enabled,create_by,create_time) values (#{roleName},#{enabled},#{createBy},#{createTime,jdbcType=TIMESTAMP}    )")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert2(SysRole sysRole);


    /**
     * 返回非自增主键
     *
     * @param sysRole
     * @return
     */
    @Insert("insert into sys_role (role_name,enabled,create_by,create_time) values (#{roleName},#{enabled},#{createBy},#{createTime,jdbcType=TIMESTAMP}    )")
    @SelectKey(statement = "select LAST_INSERT_ID()", keyProperty = "id", resultType = Long.class, before = false)
    int insert3(SysRole sysRole);

    /**
     * 修改
     *
     * @param sysRole
     * @return
     */
    @Update({"update sys_role set role_name=#{roleName} ,enabled=#{enabled} ,create_by=#{createBy} ,create_time=#{createTime,jdbcType=TIMESTAMP} "})
    int updateById(SysRole sysRole);

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @Delete("delete from sys_role where id = #{id}")
    int deleteById(Long id);


    /**
     * 查询所有角色，以及角色对应的权限信息
     *
     * @return
     */
    List<SysRole> selectAllRoleAndPrivileges();

    /**
     * 根据 用户Id获取用户的角色信息
     *
     * @param userId
     * @return
     */
    List<SysRole> selectRoleUserIdChoose(Long userId);


}
