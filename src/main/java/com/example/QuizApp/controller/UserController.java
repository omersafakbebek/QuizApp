package com.example.QuizApp.controller;


import com.example.QuizApp.dto.request.create.UserCreateDTO;
import com.example.QuizApp.dto.request.update.UserUpdateDTO;

import com.example.QuizApp.dto.response.UserDetailResponseDTO;
import com.example.QuizApp.dto.response.UserResponseDTO;

import com.example.QuizApp.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.access.prepost.PreAuthorize;


import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

import java.security.Principal;

import java.util.List;

@RestController
@RequestMapping("user")
@PreAuthorize("isAuthenticated()")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping(path = "/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponseDTO> getUsers(){
        return userService.getUsers();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path="/{id}")
    public UserDetailResponseDTO getUser(@PathVariable("id") Long id){
        return userService.getUser(id);
    }
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponseDTO createUser(@Valid @RequestBody UserCreateDTO userDTO){
        return userService.createUser(userDTO);
    }
    @PutMapping(path="/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void updateUser(@PathVariable("id") Long id,@RequestBody UserUpdateDTO userDTO){
        userService.updateUser(id,userDTO);
    }
    @DeleteMapping(path="/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(@PathVariable("id") Long id){
        userService.deleteUser(id);
    }

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response,Principal principal) throws IOException {
        userService.refreshToken(request,response,principal);
    }
    @PostMapping("/{username}/role/{rolename}")
    @PreAuthorize("hasRole('ADMIN')")
    public void addRole(@PathVariable("username") String username,@PathVariable("rolename") String rolename){
        userService.addRoleToUser(username,rolename);
    }

    @GetMapping
    public UserDetailResponseDTO getUser(Principal principal){
        return userService.getUser(principal);
    }
    @PutMapping
    public void updateUser(Principal principal,@RequestBody UserUpdateDTO userDTO){
        userService.updateUser(principal,userDTO);
    }
    @DeleteMapping
    public void deleteUser(Principal principal){
        userService.deleteUser(principal);
    }



}
