package com.example.QuizApp.exception;

import com.example.QuizApp.errorCode.ErrorCode;

public class ServiceException extends RuntimeException{
    ErrorCode code;
    public ServiceException(ErrorCode code, String message){
        super(message);
        this.code=code;
    }

    public ErrorCode getCode() {
        return code;
    }
}
