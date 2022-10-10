package com.example.QuizApp.security;

import com.example.QuizApp.filter.CustomAuthenticationFilter;
import com.example.QuizApp.filter.CustomAuthorizationFilter;
import com.example.QuizApp.filter.FilterChainExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration @EnableWebSecurity @RequiredArgsConstructor @EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider();
        daoAuthenticationProvider.setHideUserNotFoundExceptions(false);
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder);
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
//        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
        auth.authenticationProvider(daoAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        CustomAuthenticationFilter customAuthenticationFilter=new CustomAuthenticationFilter(resolver, authenticationManagerBean());
        FilterChainExceptionHandler filterChainExceptionHandler=new FilterChainExceptionHandler(resolver);
        http.formLogin().loginPage("/login");
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//        http.requestMatchers().antMatchers("/login/**").and().authorizeRequests().anyRequest().permitAll().and().httpBasic().and().exceptionHandling().authenticationEntryPoint(authEntryPoint);
        http.authorizeRequests().antMatchers("/login/**","/swagger-ui/**","/swagger-resources/**","/v2/api-docs/**").permitAll();
//        http.authorizeRequests().antMatchers(GET,"/user/{id}/**").hasAnyAuthority("ROLE_USER","ROLE_ADMIN");
//        http.authorizeRequests().antMatchers(GET,"/user/**").hasAnyAuthority("ROLE_ADMIN");
//        http.authorizeRequests().antMatchers(POST,"/user/**").hasAnyAuthority("ROLE_ADMIN");
//        http.authorizeRequests().antMatchers(PUT,"/user/{id}/**").hasAnyAuthority("ROLE_ADMIN","ROLE_USER");
//        http.authorizeRequests().antMatchers(DELETE,"/user/{id}/**").hasAnyAuthority("ROLE_ADMIN","ROLE_USER");

//        http.authorizeRequests().antMatchers(GET,"/quiz/{id}/**").hasAnyAuthority("ROLE_ADMIN","ROLE_USER");
//        http.authorizeRequests().antMatchers(GET,"/quiz/**").hasAnyAuthority("ROLE_ADMIN");
//        http.authorizeRequests().antMatchers(POST,"/quiz/**").hasAnyAuthority("ROLE_ADMIN","ROLE_USER");
//        http.authorizeRequests().antMatchers(PUT,"/quiz/**").hasAnyAuthority("ROLE_ADMIN","ROLE_USER");
//        http.authorizeRequests().antMatchers(DELETE,"/quiz/**").hasAnyAuthority("ROLE_ADMIN,ROLE_USER");

//        http.authorizeRequests().anyRequest().authenticated();

        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(), CustomAuthenticationFilter.class);
        http.addFilterBefore(filterChainExceptionHandler,CustomAuthorizationFilter.class);


    }
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }
}
