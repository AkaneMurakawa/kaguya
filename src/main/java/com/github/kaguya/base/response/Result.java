package com.github.kaguya.base.response;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

/**
 * 统一响应
 */
@Data
public class Result<T> implements Serializable {

    private static final Long serialVersionUID = 1L;
    private static final String SUCCESS_CODE = "1";
    private static final String FAIL_CODE = "0";
    private static final String SUCCESS_MSG = "success";
    private static final String FAIL_MSG = "fail";

    /**
     * 结果
     */
    private String code;
    /**
     * 描述
     */
    private String msg;
    /**
     * 错误结果集
     */
    private List<ErrorMsg> errors;
    /**
     * 返回的数据结果集
     */
    private T data;

    public void setDefaultSuccessResult() {
        setCode(SUCCESS_CODE);
        setMsg(SUCCESS_MSG);
    }

    public void setDefaultFailResult() {
        setCode(FAIL_CODE);
        setMsg(FAIL_MSG);
    }

    public static Result success() {
        Result response = new Result();
        response.setDefaultSuccessResult();
        return response;
    }

    public static Result success(Object data) {
        Result response = new Result();
        response.setData(data);
        response.setDefaultSuccessResult();
        return response;
    }

    public static Result fail() {
        Result response = new Result();
        response.setDefaultFailResult();
        return response;
    }

    public static Result fail(String msg) {
        Result response = new Result();
        response.setCode(FAIL_CODE);
        response.setMsg(msg);
        return response;
    }

    public static Result fail(List<ErrorMsg> errors) {
        Result response = Result.fail();
        response.setErrors(errors);
        return response;
    }

    public static Result fail(String msg, List<ErrorMsg> errors) {
        Result response = Result.fail(msg);
        response.setErrors(errors);
        return response;
    }

}
