package com.boot.study.model;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Table(name = "sys_user")
public class SysUser implements Serializable {

    private static final long serialVersionUID = -5424961325750528661L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name")
    @NotBlank
    @Length(min = 3,max = 20,message = "用户名长度必须在3-20之间")
    private String userName;

    @Column(name = "password")
    @NotBlank
    @Size(min = 6,max = 6,message = "用户密码长度为6")
    private String password;

    @Column(name = "sex")
    private String sex;

    @Column(name = "age")
    private Integer age;


}
