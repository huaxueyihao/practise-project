package com.boot.study.controller;

import com.boot.study.common.JSONResponse;

public class BaseController {

    public JSONResponse success(Object data) {
        return JSONResponse.builder().code(200).msg("ok").data(data).build();
    }

    public JSONResponse failure(Object data, long errCode, String msg) {
        return JSONResponse.builder().code(errCode).msg(msg).data(data).build();
    }

}
