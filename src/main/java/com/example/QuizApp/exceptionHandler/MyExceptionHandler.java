package com.example.QuizApp.exceptionHandler;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.QuizApp.errorCode.ErrorCode;
import com.example.QuizApp.exception.ServiceException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@ControllerAdvice
public class MyExceptionHandler {

    private final MessageSource messageSource;

    @Autowired
    public MyExceptionHandler(ResourceBundleMessageSource messageSource, LocaleResolver localeResolver){
        this.messageSource=messageSource;

    }

    @ExceptionHandler(value = AccessDeniedException.class)
    protected void handleAccessDeniedException(HttpServletResponse response,RuntimeException ex) throws IOException{
        response.setHeader("error", ex.getLocalizedMessage());
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        Map<String,String> error = new HashMap<>();
        error.put("error_message",ex.getLocalizedMessage());
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(),error);
    }
    @ExceptionHandler(value = JWTVerificationException.class)
    protected void authorizationDenied(HttpServletResponse response,RuntimeException ex) throws IOException {
        response.setHeader("error", ex.getLocalizedMessage());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        Map<String,String> error = new HashMap<>();
        error.put("error_message",ex.getLocalizedMessage());
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(),error);
    }
    @ExceptionHandler(value = IllegalStateException.class)
    protected void illState(HttpServletResponse response,RuntimeException ex) throws IOException{
        response.setHeader("error_type", ex.getClass().getCanonicalName());
        Map<String, String> errors = new HashMap<>();
        errors.put("error_message", ex.getMessage());
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(),errors);
    }
    @ExceptionHandler({ AuthenticationException.class })
    public void handleAuthenticationException(HttpServletResponse response,Exception ex) throws IOException {
        response.setHeader("error type", ex.getClass().getCanonicalName());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        Map<String,String> error = new HashMap<>();
        error.put("error_message",ex.getLocalizedMessage());
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(),error);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public void handleConstraintException(HttpServletResponse response,Exception ex) throws IOException{
        String message= ex.getMessage();
        if(message.contains("constraint")){
            String constraintType=message.substring(message.indexOf("constraint")+"constraint [".length(),message.lastIndexOf("]"));
            if(constraintType.equals("user_username_unique")){
                message="This username is taken.";
            }else if(constraintType.equals("user_email_unique")){
                message="A user with this email exists.";
            }
        }
        response.setHeader("error type", ex.getClass().getCanonicalName());
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        Map<String,String> error = new HashMap<>();
        error.put("error_message",message);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(),error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected void handleMethodArgumentNotValid(MethodArgumentNotValidException ex,HttpServletResponse response) throws IOException {
        response.setHeader("error_type", ex.getClass().getCanonicalName());
        Map<String, String> errors = new HashMap<>();
        StringBuilder sb=new StringBuilder("Validation error(s): ");
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String errorMessage = error.getDefaultMessage();
            sb.append("[").append(errorMessage).append("] ");
        });
        errors.put("error_message",sb.toString());
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(),errors);
    }
    @ExceptionHandler(ServiceException.class)
    protected void handleServiceException(HttpServletResponse response, ServiceException ex, HttpServletRequest request,Locale locale) throws IOException {

        response.setHeader("error_type", ex.getClass().getCanonicalName());
        ErrorCode errorCode=ex.getCode();
        response.setStatus(errorCode.getStatus().value());
        Map<String,String> error = new HashMap<>();
        error.put("error_message",toLocale(errorCode.getCode(),locale));
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(),error);
    }
    @ExceptionHandler(Throwable.class)
    protected void handleExceptions(HttpServletResponse response,Exception ex) throws  IOException{
        response.setHeader("error_type", ex.getClass().getCanonicalName());
        Map<String, String> errors = new HashMap<>();
        errors.put("error_message", ex.getMessage());
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(),errors);
    }
    public String toLocale(String code,Locale locale){

        return messageSource.getMessage(code,null,locale);
    }

}

