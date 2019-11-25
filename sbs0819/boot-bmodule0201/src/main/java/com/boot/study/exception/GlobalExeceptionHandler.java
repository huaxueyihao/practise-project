package com.boot.study.exception;

import com.boot.study.common.JSONResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 统一异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExeceptionHandler {


    /**
     * 用于controller参数校验的异常
     * 项目中没有用到
     * @param e
     * @return
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public JSONResponse handleValidationParamterException(MethodArgumentNotValidException e) {
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        for (ObjectError error : allErrors) {
            return JSONResponse.builder().code(400).msg(error.getObjectName() + " " + error.getDefaultMessage()).success(false).build();
        }
        return JSONResponse.builder().code(400).msg("未知错误").success(false).build();
    }

    /**
     * 自定义业务异常
     * @param exeception
     * @param request
     * @return
     */
    @ExceptionHandler(BusinessException.class)
    public JSONResponse handleBusinessException(BusinessException exeception, HttpServletRequest request){
        log.error("BusinessException:{}",exeception);
        return JSONResponse.builder().code(exeception.getErrorCode()).msg(exeception.getMessage()).success(false).build();
    }


}
