package com.example.QuizApp.errorCode;

import org.springframework.http.HttpStatus;

public enum UserCode implements ErrorCode{
    UserNotFoundUsername("user-1",HttpStatus.NOT_FOUND),
    UserNotFoundId("user-2",HttpStatus.NOT_FOUND),
    RoleNotFoundName("user-3",HttpStatus.NOT_FOUND);
    final String code;
    final HttpStatus status;

    UserCode(String code, HttpStatus status) {
        this.code=code;
        this.status=status;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public HttpStatus getStatus() {
        return this.status;
    }
}
