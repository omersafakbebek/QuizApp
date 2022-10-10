package com.example.QuizApp.config;
import com.example.QuizApp.model.Answer;
import com.example.QuizApp.model.Quiz;
import com.example.QuizApp.repository.AnswerRepository;
import com.example.QuizApp.repository.QuizRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class QuizConfig {
    @Bean
    CommandLineRunner commandLineRunner(QuizRepository quizRepository, AnswerRepository answerRepository){
        return args ->{
            if (quizRepository.count() == 0) {

                Quiz first = new Quiz();

                first.setQuestion("How old are you");
                Quiz second = new Quiz();

                second.setQuestion("What is your name");
                Answer firstAnswer = Answer.builder().answer("1").quiz(first).build();
                Answer secondAnswer = Answer.builder().answer("2").quiz(first).build();
                Answer thirdAnswer = Answer.builder().answer("Safak").quiz(second).build();
                Answer fourthAnswer = Answer.builder().answer("Özer").quiz(second).build();
                quizRepository.saveAll(Arrays.asList(first, second));
                answerRepository.saveAll(Arrays.asList(firstAnswer, secondAnswer, thirdAnswer, fourthAnswer));
            }
        };
    }

}
