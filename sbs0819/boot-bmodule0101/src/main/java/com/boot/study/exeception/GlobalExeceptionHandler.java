package com.boot.study.exeception;

import com.boot.study.common.JSONResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExeceptionHandler {


    @ExceptionHandler({MethodArgumentNotValidException.class})
    public JSONResponse handleValidationParamterException(MethodArgumentNotValidException e) {
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();

        for (ObjectError error : allErrors) {
            return JSONResponse.builder().code(400).msg(error.getObjectName() + " " + error.getDefaultMessage()).build();
        }
        return JSONResponse.builder().code(400).msg("未知错误").build();
    }

    @ExceptionHandler(BusinessException.class)
    public JSONResponse handleBusinessException(BusinessException exeception, HttpServletRequest request){
        log.error("BusinessException:{}",exeception);
        return JSONResponse.builder().code(exeception.getErrorCode()).msg(exeception.getMessage()).build();
    }


}
