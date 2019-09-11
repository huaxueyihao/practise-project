package com.boot.study.common;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * 新增菜单时的tree数据结构对象
 */
@Data
@Builder
@ToString
public class TreeDto {

    private Long id;

    private String title;

    private Long parentId;

    private List<TreeDto> children;

}
