package com.example.QuizApp.mapper;

import com.example.QuizApp.dto.request.create.UserCreateDTO;
import com.example.QuizApp.dto.request.update.UserUpdateDTO;
import com.example.QuizApp.dto.response.UserDetailResponseDTO;
import com.example.QuizApp.dto.response.UserResponseDTO;
import com.example.QuizApp.model.User;
import org.mapstruct.*;

import java.util.List;

@Mapper(config = SharedConfig.class)
public interface UserMapper {
    @Named(value = "userResponseDTO")
    UserResponseDTO entityToResponseDTO(User user);

    UserDetailResponseDTO entityToDetailResponseDTO(User user);


    User DTOToEntity(UserCreateDTO userCreateDTO);

    @IterableMapping(qualifiedByName = "userResponseDTO")
    List<UserResponseDTO> responseDTOList(List<User> user);

    void merge(@MappingTarget User user, UserUpdateDTO userUpdateDTO);
}
