package com.boot.study.mapper;

import com.boot.study.common.MyMapperSuppurt;
import com.boot.study.model.SysMenu;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SysMenuMapper extends MyMapperSuppurt<SysMenu> {

    List<SysMenu> selectByParentId(@Param("parentId") Long parentId);
}
