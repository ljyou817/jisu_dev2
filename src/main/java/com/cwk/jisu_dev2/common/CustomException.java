package com.cwk.jisu_dev2.common;

/**
 * 自定义业务异常
 * @author zzb04
 */
public class CustomException extends RuntimeException {
    public CustomException(String msg){
        super(msg);
    }
}
