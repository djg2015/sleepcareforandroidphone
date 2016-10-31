package com.ewell.android.common.exception;

/**
 * Created by Dongjg on 2016-7-12.
 */
public class EwellException extends  Exception {
    private ExceptionEnum _exceptionEnum;
    public ExceptionEnum getExceptionEnum() {
        return _exceptionEnum;
    }

    public String get_exceptionMsg(){return _exceptionMsg;}
    private String _exceptionMsg="";


//    public String getExceptionMsg() {
//        switch(_exceptionEnum){
//            case XmppBusinessError:
//                _exceptionMsg="远程业务内部异常";
//                break;
//            case UnknowResponse:
//                _exceptionMsg="返回的响应数据无法识别";
//                break;
//            case TimeOutError:
//                _exceptionMsg="请求超时";
//                break;
//            case NetError:
//                _exceptionMsg="网络连接异常";
//                break;
//            case SystemError:
//                _exceptionMsg="系统异常";
//                break;
//
//            default:
//                _exceptionMsg="操作失败";
//                break;
//
//        }
//
//        return _exceptionMsg;
//    }

    private StackTraceElement[] _stack;
    public StackTraceElement[] getStack() {
        return _stack;
    }

    public EwellException(ExceptionEnum kind,String exceptionMsg) {
        this._exceptionMsg = exceptionMsg;
        this._exceptionEnum = kind;
    }

    public EwellException(ExceptionEnum kind) {
        this._exceptionEnum = kind;
    }

    public EwellException(String exceptionMsg) {
        this._exceptionMsg = exceptionMsg;

    }

    public EwellException(Exception ex) {
        this._exceptionEnum = ExceptionEnum.SystemError;
        this._exceptionMsg = ex.getMessage();
        this._stack = ex.getStackTrace();
    }
}
