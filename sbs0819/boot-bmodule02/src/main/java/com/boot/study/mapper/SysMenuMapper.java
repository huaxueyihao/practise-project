package com.boot.study.mapper;

import com.boot.study.model.SysMenu;
import com.boot.study.common.MyMapperSuppurt;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysMenuMapper extends MyMapperSuppurt<SysMenu> {

    List<SysMenu> selectByParentId(@Param("parentId") Long parentId);

    void updateLeafNodeById(@Param("leafNode") Integer leafNode, @Param("id") Long id);

}
