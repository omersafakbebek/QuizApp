package com.example.QuizApp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
public class QuizDetailResponseDTO {
    private Long id;
    private String question;
    private List<AnswerResponseDTO> answers;
}
