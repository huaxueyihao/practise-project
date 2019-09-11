package com.boot.study.bean;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@Builder
public class MiniMenuTreeDto {

    //菜单名称
    private String title;
    //连接
    private String href;
    //小图标
    private String icon;
    // 打开方式
    private String target = "_self";
    //菜单id
    private long menuId;
    // 父菜单id
    private long parentId;

    private List<MiniMenuTreeDto> child;


}
