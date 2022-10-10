package com.example.QuizApp.config;

import com.example.QuizApp.model.Role;
import com.example.QuizApp.model.User;
import com.example.QuizApp.repository.RoleRepository;
import com.example.QuizApp.repository.UserRepository;
import com.example.QuizApp.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static java.time.Month.*;

@Configuration
public class UserConfig {
    @Bean
    CommandLineRunner commandLineRunner2(UserRepository repository, RoleRepository roleRepository, UserService userService, PasswordEncoder passwordEncoder){
        return args ->{
            if(repository.count()==0) {
                roleRepository.save(new Role(null,"ROLE_USER"));
                roleRepository.save(new Role(null,"ROLE_ADMIN"));
                User first = new User("user1", passwordEncoder.encode("password1"), "name1", "surname1", "email1", LocalDate.of(2000, AUGUST, 5));
                User second = new User("user2", passwordEncoder.encode("password2"), "name2", "surname2", "email2", LocalDate.of(1998, NOVEMBER, 4));
                repository.saveAll(Arrays.asList(first,second));
                userService.addRoleToUser("user1","ROLE_USER");
                userService.addRoleToUser("user2","ROLE_ADMIN");
            }
        };
    }
}
