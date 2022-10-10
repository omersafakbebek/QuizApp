package com.example.QuizApp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.persistence.*;
@Table
@Entity
@Builder
@AllArgsConstructor
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String answer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="quiz_id",referencedColumnName = "id")
    private Quiz quiz;
    public Answer(String answer) {
        this.answer=answer;
    }


    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public Answer() {

    }
    public void setAnswer(String answer){this.answer=answer;}
    public String getAnswer() {
        return answer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


}
