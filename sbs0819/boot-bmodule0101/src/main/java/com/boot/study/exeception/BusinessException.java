package com.boot.study.exeception;

/**
 * 业务异常对象
 */
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 600183292785742603L;

    private long errorCode;

    private String[] errorMsg;


    public long getErrorCode() {
        return errorCode;
    }


    public void setErrorCode(long errorCode) {
        this.errorCode = errorCode;
    }

    public String[] getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String[] errorMsg) {
        this.errorMsg = errorMsg;
    }

    public BusinessException() {
    }

    public BusinessException(long errorCode,String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public BusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
