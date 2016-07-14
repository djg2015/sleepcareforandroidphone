package com.ewell.android.common.exception;

/**
 * Created by Dongjg on 2016-7-12.
 */
public class EwellException extends  Exception {
    private ExceptionEnum _exceptionEnum;

    public ExceptionEnum getExceptionEnum() {
        return _exceptionEnum;
    }

    private String _exceptionMsg;
    public String getExceptionMsg() {
        return _exceptionMsg;
    }

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

    public EwellException(Exception ex) {
        this._exceptionEnum = ExceptionEnum.SystemError;
        this._exceptionMsg = ex.getMessage();
        this._stack = ex.getStackTrace();
    }
}
