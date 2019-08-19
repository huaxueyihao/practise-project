package com.boot.study.model;

import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class SysUser implements Serializable {

    private static final long serialVersionUID = -5424961325750528661L;

    private Long id;

    private String userName;

    private String sex;

    private Integer age;

}
