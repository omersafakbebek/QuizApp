package com.example.QuizApp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class QuizResponseDTO {
    private Long id;
    private String question;

}
