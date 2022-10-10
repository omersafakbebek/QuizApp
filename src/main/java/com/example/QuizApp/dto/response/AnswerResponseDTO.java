package com.example.QuizApp.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder

public class AnswerResponseDTO {
    private Long id;
    private String answer;
    @JsonIgnore
    private QuizResponseDTO quiz;
}
