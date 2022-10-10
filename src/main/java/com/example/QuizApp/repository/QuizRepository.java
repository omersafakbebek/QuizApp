package com.example.QuizApp.repository;

import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaRepository;
import com.example.QuizApp.model.Quiz;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuizRepository extends EntityGraphJpaRepository<Quiz,Long> {


    Optional<Quiz> findQuizByQuestion(String question);
}
