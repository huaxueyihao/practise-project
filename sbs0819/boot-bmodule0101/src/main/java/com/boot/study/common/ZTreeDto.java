package com.boot.study.common;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * 新增菜单时的ztree数据结构对象
 */
@Data
@Builder
@ToString
public class ZTreeDto {

    private Long id;

    private String name;

    private Long parentId;

    private boolean open = true;

    private List<ZTreeDto> children;

}
