package com.example.QuizApp.errorCode;

import org.springframework.http.HttpStatus;

public enum QuizCode implements ErrorCode {
    QuizNotFoundId("quiz-1", HttpStatus.NOT_FOUND),
    QuizFoundQuestion("quiz-2",HttpStatus.BAD_REQUEST),
    AnswerNotFoundId("quiz-3",HttpStatus.NOT_FOUND),
    QuizForbidden("quiz-4",HttpStatus.FORBIDDEN);
    final String code;
    final HttpStatus status;
    QuizCode(String code, HttpStatus status) {
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
