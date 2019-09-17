package com.boot.study.bean;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlanTypeSelectDto {

    private String name;

    private String value;


}
