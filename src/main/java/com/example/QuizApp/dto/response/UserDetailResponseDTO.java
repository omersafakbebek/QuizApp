package com.example.QuizApp.dto.response;

import com.example.QuizApp.model.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Builder
@Data

@AllArgsConstructor
public class UserDetailResponseDTO {
    private Long id;
    private String userName;
    private String email;
    private String name;
    private String surname;
    private String password;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dob;
    private Integer age;
    private List<RoleResponseDTO> roles;
}
