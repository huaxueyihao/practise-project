package com.boot.study.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Table(name = "sys_plan_type")
public class SysPlanType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "type_name")
    private String typeName;

    @Column(name = "type_desc")
    private String typeDesc;

    @Column(name = "icon")
    private String icon;

    @Column(name = "bg_color")
    private String bgColor;
    @Column(name = "remark")
    private String remark;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;


}
