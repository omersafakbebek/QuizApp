package com.example.QuizApp.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data

@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class UserResponseDTO {
    private Long id;
    private String userName;
    private String email;
    private List<RoleResponseDTO> roles;



}
