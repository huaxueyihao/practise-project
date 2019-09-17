package com.boot.study.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Table(name = "sys_plan_day_execution")
public class SysPlanDayExecution implements Serializable {

    private static final long serialVersionUID = -5424961325750528661L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "title")
    private String title;

    @Column(name = "type_name")
    private String typeName;

    @Column(name = "type_desc")
    private String typeDesc;

//    @Column(name = "day_time")
//    private LocalDate dayime;

    @Column(name = "start_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    @Column(name = "plan_state")
    private String planState;

    @Column(name = "use_time")
    private Integer useTime;

    @Column(name = "remark")
    private String remark;

    @Column(name = "location")
    private String location;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;



}
