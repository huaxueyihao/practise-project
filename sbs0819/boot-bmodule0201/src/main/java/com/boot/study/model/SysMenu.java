package com.boot.study.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Table(name = "sys_menu")
public class SysMenu implements Serializable {

    private static final long serialVersionUID = 5670789560868672620L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "menu_name")
    private String menuName;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "little_icon")
    private String littleIcon;

    @Column(name = "route_url")
    private String routeUrl;

    @Column(name = "target")
    private String target;

    // 是否叶子节点，0:是，1：不是
    @Column(name = "leaf_node")
    private Integer leafNode;

    //类型，0:根目录，1:菜单，2：按钮
    @Column(name = "menu_type")
    private Integer menuType;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;





}
