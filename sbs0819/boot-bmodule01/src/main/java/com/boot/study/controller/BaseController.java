package com.boot.study.controller;

import com.boot.study.common.JSONResponse;
import com.boot.study.common.PageResult;

public class BaseController {

    public JSONResponse success(Object data) {
        return JSONResponse.builder().code(0).msg("ok").count(0).data(data).build();
    }

    public JSONResponse success(PageResult<?> pageResult) {
        return JSONResponse.builder().code(0).msg("ok").count(pageResult.getCount()).data(pageResult.getDataList()).build();
    }

    public JSONResponse failure(Object data, long errCode, String msg) {
        return JSONResponse.builder().code(errCode).msg(msg).data(data).build();
    }

}
