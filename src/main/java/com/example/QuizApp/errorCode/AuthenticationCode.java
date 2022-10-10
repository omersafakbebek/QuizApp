package com.example.QuizApp.errorCode;

import org.springframework.http.HttpStatus;

public enum AuthenticationCode implements ErrorCode{
    MissingAuthentication("auth-1",HttpStatus.UNAUTHORIZED);

    final String code;
    final HttpStatus status;
    AuthenticationCode(String code, HttpStatus status) {
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
