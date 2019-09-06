package com.boot.study.controller;

import com.boot.study.common.JSONResponse;
import com.boot.study.common.PageResult;

import java.util.Objects;

public class BaseController {

    public JSONResponse success(Object data) {
        return JSONResponse.builder().data(data)
                .msg("ok").code(0).build();
    }

    public JSONResponse success(PageResult<?> pageResult) {
        return JSONResponse.builder().data(Objects.nonNull(pageResult) ? pageResult.getDataList() : null)
                .count(Objects.nonNull(pageResult) ? pageResult.getCount() : 0).msg("ok").code(0).build();
    }

    public JSONResponse failure(Object data, long errCode, String msg) {
        return JSONResponse.builder().code(errCode).msg(msg).data(data).build();
    }

}
