package com.example.QuizApp.dto.request.create;


import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserCreateDTO {
    @NotNull(message = "User name cannot be null.")
    private String userName;
    @NotNull(message = "Password cannot be null.")
    private String password;
    private String name;
    private String surname;
    @NotNull(message = "Email cannot be null.")
    @Email(message = "Email is not valid.")
    private String email;
    private LocalDate dob;
    private List<RoleCreateDTO> roles=new ArrayList<>();
}
