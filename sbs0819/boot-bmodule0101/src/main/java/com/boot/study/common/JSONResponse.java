package com.boot.study.common;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JSONResponse {

    private long code;

    private String msg;

    private long count;

    private Object data;

    private boolean success;


}
