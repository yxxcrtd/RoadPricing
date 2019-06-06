package com.igoosd.common.model;

import lombok.Data;

/**
 * 2017/10/16.
 * 系统统一响应Model
 * code :
 *      200 : 请求正常
 *      -200： 业务异常
 *      400 ： 无效的权限--前端接收到此code 应跳转到登录界面
 *      其他  待定...
 */
@Data
public class ResultMsg<T> {

    private int code;

    private String message;

    private T data;

    private ResultMsg(){
    }

    public  static final <T> ResultMsg<T> resultSuccess(String message, T data){
        ResultMsg<T> resultVo = new ResultMsg<>();
        resultVo.setCode(200);
        resultVo.setMessage(message);
        resultVo.setData(data);
        return resultVo;
    }

    public  static final <T> ResultMsg<T> resultSuccess(String message){
        return  resultSuccess(message,null);
    }

    public static final <T> ResultMsg<T> resultSuccess(T t){
        return resultSuccess(null,t);
    }

    public static final <T> ResultMsg<T> resultSuccess(){
        return resultSuccess(null,null);
    }

    public static final <T> ResultMsg<T> resultFail(int code, String message){
        ResultMsg<T> resultVo = new ResultMsg<T>();
        resultVo.setCode(code);
        resultVo.setMessage(message);
        return resultVo;
    }

    public static final <T> ResultMsg<T> resultFail(String message){
        return resultFail(-200,message);
    }

}
