package com.boot.study.mapper;

import com.boot.study.common.MyMapperSuppurt;
import com.boot.study.model.SysMenu;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SysMenuMapper extends MyMapperSuppurt<SysMenu> {

//    @Select("select t.* from sys_menu t where t.parent_id = #{parentId}")
    List<SysMenu> selectByParentId(@Param("parentId") Long parentId);
}
