package com.boot.study.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

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



}
