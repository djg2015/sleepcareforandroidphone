package com.ewell.android.common.exception;

/**
 * Created by Dongjg on 2016-7-12.
 * 自定义异常种类
 */
public enum  ExceptionEnum {
    //远程业务内部异常
    XmppBusinessError,
    //返回的响应数据无法识别
    UnknowResponse,
    //请求超时
    TimeOutError,
    //网络连接异常
    NetError,
    //系统异常
    SystemError,
    //未知异常
    UnKnowError

}
