package com.github.kaguya.exception.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ResponseMsg implements Serializable {

    private static final Long serialVersionUID = 1L;
    private static final String SUCCESS_RESULT = "1";
    private static final String FAIL_RESULT = "0";
    private static final String SUCCESS_MSG = "success";
    private static final String FAIL_MSG = "fail";

    /**
     * 结果
     */
    private String result;
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
    private Object data;

    public void setDefaultSuccessResult() {
        setResult(SUCCESS_RESULT);
        setMsg(SUCCESS_MSG);
    }

    public void setDefaultFailResult() {
        setResult(FAIL_RESULT);
        setMsg(FAIL_MSG);
    }

    public static ResponseMsg buildSuccessResult() {
        ResponseMsg response = new ResponseMsg();
        response.setDefaultSuccessResult();
        return response;
    }

    public static ResponseMsg buildSuccessResult(Object data) {
        ResponseMsg response = new ResponseMsg();
        response.setData(data);
        response.setDefaultSuccessResult();
        return response;
    }

    public static ResponseMsg buildFailResult() {
        ResponseMsg response = new ResponseMsg();
        response.setDefaultFailResult();
        return response;
    }

    public static ResponseMsg buildFailResult(String msg) {
        ResponseMsg response = new ResponseMsg();
        response.setResult(FAIL_RESULT);
        response.setMsg(msg);
        return response;
    }

    public static ResponseMsg buildFailResult(List<ErrorMsg> errors) {
        ResponseMsg response = ResponseMsg.buildFailResult();
        response.setErrors(errors);
        return response;
    }

    public static ResponseMsg buildFailResult(String msg, List<ErrorMsg> errors) {
        ResponseMsg response = ResponseMsg.buildFailResult(msg);
        response.setErrors(errors);
        return response;
    }

}
