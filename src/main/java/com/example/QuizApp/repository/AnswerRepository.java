package com.example.QuizApp.repository;

import com.example.QuizApp.model.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnswerRepository extends JpaRepository<Answer,Long> {
    Optional<Answer> findByQuizIdAndId(Long quizId, Long answerId);
}
