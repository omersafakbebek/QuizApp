package com.example.QuizApp.mapper;

import com.example.QuizApp.dto.request.create.QuizCreateDTO;
import com.example.QuizApp.dto.request.update.AnswerUpdateDTO;
import com.example.QuizApp.dto.request.update.QuizUpdateDTO;
import com.example.QuizApp.dto.response.QuizDetailResponseDTO;
import com.example.QuizApp.dto.response.QuizResponseDTO;
import com.example.QuizApp.model.Answer;
import com.example.QuizApp.model.Quiz;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;

@Mapper(config = SharedConfig.class)
public interface QuizMapper {
    @Named(value = "quizResponseDTO")
    QuizResponseDTO entityToResponseDTO(Quiz quiz);

    Quiz DTOToEntity(QuizCreateDTO quizCreateDTO);

    QuizDetailResponseDTO entityToDetailResponseDTO(Quiz quiz);

    @IterableMapping(qualifiedByName = "quizResponseDTO")
    List<QuizResponseDTO> entityToListResponseDTO(List<Quiz> quizzes);



    void mergeQuiz(@MappingTarget Quiz quiz, QuizUpdateDTO quizUpdateDTO);

    void mergeAnswer(@MappingTarget Answer answer, AnswerUpdateDTO answerUpdateDTO);
}
