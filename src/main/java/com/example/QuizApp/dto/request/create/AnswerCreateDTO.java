package com.example.QuizApp.dto.request.create;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AnswerCreateDTO {
    @NotNull(message = "Answer cannot be null.")
    private String answer;
}
