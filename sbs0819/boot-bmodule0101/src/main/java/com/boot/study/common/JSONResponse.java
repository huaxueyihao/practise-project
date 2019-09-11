package com.boot.study.common;

import lombok.Builder;
import lombok.Data;

/**
 * 响应客户端的统一数据格式类
 */
@Data
@Builder
public class JSONResponse {

    /**
     * 错误码：这个是layui要求字段
     */
    private long code;

    /**
     * 错误信息：这个是layui要求字段
     */
    private String msg;

    /**
     * 分页的数量：这个是layui要求字段
     */
    private long count;

    /**
     * 结果对象：这个是layui要求字段
     */
    private Object data;

    /**
     * 异常标志：true：成功：false：异常
     */
    private boolean success;


}
