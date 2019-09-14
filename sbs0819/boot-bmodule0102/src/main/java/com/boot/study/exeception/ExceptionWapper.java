package com.boot.study.exeception;

import lombok.*;

import java.io.Serializable;

/**
 * 异常对象
 * 没有用到
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExceptionWapper implements Serializable {

    private static final long serialVersionUID = -2941516945587146154L;

    private String errorCode;

    private String errorMsg;

}
