package com.example.QuizApp.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.QuizApp.dto.request.create.RoleCreateDTO;
import com.example.QuizApp.dto.request.create.UserCreateDTO;
import com.example.QuizApp.dto.response.UserDetailResponseDTO;
import com.example.QuizApp.dto.response.UserResponseDTO;
import com.example.QuizApp.dto.request.update.UserUpdateDTO;
import com.example.QuizApp.errorCode.UserCode;
import com.example.QuizApp.exception.ServiceException;
import com.example.QuizApp.mapper.UserMapper;
import com.example.QuizApp.model.Role;
import com.example.QuizApp.model.User;
import com.example.QuizApp.repository.RoleRepository;
import com.example.QuizApp.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Service
@Slf4j
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(username).orElseThrow(()->new UsernameNotFoundException("User not found in the database."));
        log.info("User found in the database.");
        Collection<SimpleGrantedAuthority> authorities=new ArrayList<>();
        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        return new org.springframework.security.core.userdetails.User(user.getUserName(),user.getPassword(),authorities);
    }
    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }


    public UserDetailResponseDTO getUser(Long id) {
       return userMapper.entityToDetailResponseDTO(userRepository.findById(id).orElseThrow(()-> new ServiceException(UserCode.UserNotFoundId,"User with ID: "+id+" is not found.")));
    }

    public List<UserResponseDTO> getUsers() {
        return userMapper.responseDTOList(userRepository.findAll());
    }

    @Transactional
    public UserResponseDTO createUser(UserCreateDTO userDTO) {
        User user = userMapper.DTOToEntity(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        for(RoleCreateDTO roleDTO:userDTO.getRoles()){
            Role role=roleRepository.findByName(roleDTO.getName()).orElseThrow(()->new ServiceException(UserCode.RoleNotFoundName,"Wrong role name"));
            user.getRoles().add(role);
        }
       return userMapper.entityToResponseDTO(user);

    }
    @Transactional
    public void updateUser(Long id, UserUpdateDTO userDTO) {
        User user=userRepository.findById(id).orElseThrow(()-> new ServiceException(UserCode.UserNotFoundId,"Wrong id"));
        userMapper.merge(user,userDTO);
        userRepository.save(user);
    }
    @Transactional
    public void deleteUser(Long id) {
        User user=userRepository.findById(id).orElseThrow(()-> new ServiceException(UserCode.UserNotFoundId,"Wrong id"));
        userRepository.delete(user);
    }

    @Transactional
    public void addRoleToUser(String username, String roleName) {
        log.info("Adding role {} to user {}",roleName,username);
        User user = userRepository.findByUserName(username).orElseThrow(()-> new ServiceException(UserCode.UserNotFoundUsername,"Wrong username"));
        Role role = roleRepository.findByName(roleName).orElseThrow(()-> new ServiceException(UserCode.RoleNotFoundName,"Wrong role name"));
        user.getRoles().add(role);
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response, Principal principal) throws IOException {
        User user = userRepository.findByUserName(principal.getName()).orElseThrow(()->new ServiceException(UserCode.UserNotFoundUsername,"Wrong username"));
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        String access_token = JWT.create()
                .withSubject(user.getUserName())
                .withExpiresAt(new Date(System.currentTimeMillis()+10*60*1000))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles",user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                .sign(algorithm);
        String refresh_token=JWT.create()
                .withSubject(user.getUserName())
                .withExpiresAt(new Date(System.currentTimeMillis()+30*60*1000))
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);

        Map<String,String > tokens=new HashMap<>();
        tokens.put("access_token",access_token);
        tokens.put("refresh_token",refresh_token);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(),tokens);
    }
    public UserDetailResponseDTO getUser(Principal principal){
        User user = userRepository.findByUserName(principal.getName()).orElseThrow(()->new ServiceException(UserCode.UserNotFoundUsername,"Wrong username"));
        return userMapper.entityToDetailResponseDTO(user);
    }
    @Transactional
    public void updateUser(Principal principal, UserUpdateDTO userDTO) {
        User user = userRepository.findByUserName(principal.getName()).orElseThrow(()->new ServiceException(UserCode.UserNotFoundUsername,"Wrong username"));
        userMapper.merge(user,userDTO);
        userRepository.save(user);
    }
    @Transactional
    public void deleteUser(Principal principal) {
        User user = userRepository.findByUserName(principal.getName()).orElseThrow(()->new ServiceException(UserCode.UserNotFoundUsername,"Wrong username"));
        userRepository.delete(user);
    }
}
