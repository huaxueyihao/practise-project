package com.boot.study.bean;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@Builder
@ToString
public class MenuTreeDto {

    //菜单id
    private Long id;
    //菜单标题
    private String meunTitle;
    //菜单地址
    private String meunUrl;
    //菜单状态
    private int meunStatus;
    //是否是叶子节点
    private boolean leafAble;
    //菜单排序
    private int meunSort;
    //子菜单集合
    private List<MenuTreeDto> childrenList;


}
