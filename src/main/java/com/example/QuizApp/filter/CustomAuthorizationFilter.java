package com.example.QuizApp.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.QuizApp.errorCode.AuthenticationCode;
import com.example.QuizApp.exception.ServiceException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info(request.getServletPath());
        if(request.getServletPath().equals("/login")||request.getServletPath().startsWith("/swagger-ui")||request.getServletPath().startsWith("/swagger-resources")||request.getServletPath().startsWith("/v2/api-docs")){
            log.info("Filter 2 giriş");
            filterChain.doFilter(request,response);
            log.info("Filter 2 ok");
        }else{
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            if(authorizationHeader!=null&&authorizationHeader.startsWith("Bearer ")){
//                try{
                    String token = authorizationHeader.substring("Bearer ".length());
                    Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                    JWTVerifier verifier= JWT.require(algorithm).build();
                    DecodedJWT decodedJWT=verifier.verify(token);
                    String username=decodedJWT.getSubject();
                    UsernamePasswordAuthenticationToken authenticationToken;
                    if(!request.getServletPath().equals("/user/token/refresh")){
                        String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
                        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                        stream(roles).forEach(role->{
                            authorities.add(new SimpleGrantedAuthority(role));
                        });
                        authenticationToken=new UsernamePasswordAuthenticationToken(username,null,authorities);
                    }else{

                        authenticationToken=new UsernamePasswordAuthenticationToken(username,null,null);
                    }

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                    filterChain.doFilter(request,response);


//                }
//                catch (Exception exception){
//                    log.info("Error type: {}",exception.getClass().getCanonicalName());
//                    log.error("Error logging in: {}",exception.getMessage());
//                    response.setHeader("error",exception.getMessage());
//                    //response.sendError(FORBIDDEN.value());
//                    response.setStatus(FORBIDDEN.value());
//                    Map<String,String > error=new HashMap<>();
//                    error.put("error_message",exception.getMessage());
//                    response.setContentType(APPLICATION_JSON_VALUE);
//                    new ObjectMapper().writeValue(response.getOutputStream(),error);
//                }

            }else{
                throw new ServiceException(AuthenticationCode.MissingAuthentication,"Security token is missing.");
            }
        }
    }
}
