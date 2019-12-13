package com.github.kaguya.exception.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 错误信息类
 */
@Data
@AllArgsConstructor
public class ErrorMsg implements Serializable {

    private static final Long serialVersionUID = 1L;

    /**
     * 错误码
     */
    private String code;
    /**
     * 错误描述信息
     */
    private String msg;
    /**
     * 错误数据
     */
    private String data;

}
