package com.example.QuizApp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;


@Entity
@Builder
@AllArgsConstructor
@Table(name = "users",uniqueConstraints = {@UniqueConstraint(name="user_email_unique",columnNames = "email"),@UniqueConstraint(name="user_username_unique",columnNames = "user_name")})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false,name = "user_name")
    private String userName;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @OrderBy("id ASC")

    private final Set<Role> roles=new HashSet<>();

    @OneToMany(cascade=CascadeType.ALL,mappedBy = "user")
    @OrderBy("id ASC")
    private final Set<Quiz> quizzes=new HashSet<>();
    @Column(nullable = false)
    private String password;
    @Column(nullable = true)
    private String name;
    private String surname;
    @Column(nullable = false,name = "email")
    private String email;
    private LocalDate dob;
    @Transient
    private Integer age;

    public User(Long id, String userName, String password, String name, String surname, String email, LocalDate dob) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.dob = dob;
    }

    public User(String userName, String password, String name, String surname, String email, LocalDate dob) {
        this.userName = userName;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.dob = dob;
    }

    public User() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Quiz> getQuizzes(){
        return this.quizzes;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", dob=" + dob +
                ", age=" + age +
                '}';
    }

    public Integer getAge() {
        return Period.between(this.dob,LocalDate.now()).getYears();
    }
    public void setAge(Integer age){
        this.age=age;
    }


    public Set<Role> getRoles() {
        return this.roles;
    }
}
