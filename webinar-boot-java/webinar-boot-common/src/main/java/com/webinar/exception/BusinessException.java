package com.webinar.exception;

import com.webinar.reApi.ApiCode;
import lombok.Data;

/**
 * 自定义异常类
 */
@Data
public class BusinessException extends RuntimeException{

    private int code;

    private String msg;


    public BusinessException(ApiCode apiCode) {
        super(apiCode.getMsg());
        this.code = apiCode.getCode();
        this.msg = apiCode.getMsg();
    }


}
