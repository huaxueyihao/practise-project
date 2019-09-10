package com.boot.study.model;

import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Table(name = "sys_file")
public class SysFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "path")
    private String path;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "extension")
    private String extension;

    // 模块：某个功能模块的文件
    @Column(name = "module")
    private String module;
    // 模块引用的主键id
    @Column(name = "moduleId")
    private Long moduleId;

    // 文件大小
    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "remark")
    private String remark;


}
