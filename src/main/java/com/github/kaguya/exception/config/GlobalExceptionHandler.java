package com.github.kaguya.exception.config;

import com.github.kaguya.exception.BusinessException;
import com.github.kaguya.exception.model.ResponseMsg;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理类
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 默认的异常处理
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseMsg defaultExceptionHandler(Exception e) throws Exception {
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

    /**
     * 业务的异常处理
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public ResponseMsg RuntimeExceptionHandler(RuntimeException e) throws RuntimeException {
        return ResponseMsg.buildFailResult("程序内部错误");

    }
}
