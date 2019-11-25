package com.boot.study.mapper;

import com.boot.study.common.MyMapperSupport;
import com.boot.study.model.SysMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysMenuMapper extends MyMapperSupport<SysMenu> {

    List<SysMenu> selectByParentId(@Param("parentId") Long parentId);

    void updateLeafNodeById(@Param("leafNode") Integer leafNode, @Param("id") Long id);

}
