package com.github.kaguya.exception;

import com.github.kaguya.exception.model.ResponseMsg;
import lombok.Getter;

public class BusinessException extends RuntimeException {

    @Getter
    private ResponseMsg responseMsg;

    public BusinessException() {
        super();
    }

    public BusinessException(ResponseMsg responseMsg) {
        super(responseMsg.getMsg());
        this.responseMsg = responseMsg;
    }
}
