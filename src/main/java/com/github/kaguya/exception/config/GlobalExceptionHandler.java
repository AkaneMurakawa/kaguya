package com.github.kaguya.exception.config;

import com.github.kaguya.exception.BusinessException;
import com.github.kaguya.exception.model.ResponseMsg;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理类
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 默认的异常处理
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseMsg defaultExceptionHandler(Exception e) throws Exception{
        return ResponseMsg.buildFailResult("System error!");
    }

    /**
     * 业务的异常处理
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public ResponseMsg BusinessExceptionHandler(BusinessException e) throws BusinessException {
        return e.getResponseMsg();

    }
}
