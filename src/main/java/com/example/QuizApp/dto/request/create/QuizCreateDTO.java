package com.example.QuizApp.dto.request.create;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;


@Data
public class QuizCreateDTO {
    @NotNull(message = "Question cannot be null.")
    private String question;
    private List<AnswerCreateDTO> answers=new ArrayList<>();
}
