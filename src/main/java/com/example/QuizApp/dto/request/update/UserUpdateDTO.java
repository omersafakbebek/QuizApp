package com.example.QuizApp.dto.request.update;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserUpdateDTO {
    private String userName;
    private String password;
    private String name;
    private String surname;
    private String email;
    private LocalDate dob;
}
