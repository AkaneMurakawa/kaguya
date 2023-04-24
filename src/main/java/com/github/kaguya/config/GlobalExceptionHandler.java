package com.github.kaguya.config;

import com.github.kaguya.base.exception.BusinessException;
import com.github.kaguya.base.response.Result;
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
    public Result defaultExceptionHandler(Exception e) {
        return Result.fail("系统错误");
    }

    /**
     * 业务的异常处理
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public Result BusinessExceptionHandler(BusinessException e) throws BusinessException {
        return Result.fail(e.getMessage());
    }

    /**
     * 运行时的异常处理
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public Result RuntimeExceptionHandler(RuntimeException e) throws RuntimeException {
        return Result.fail("程序内部错误");
    }
}
