package com.example.QuizApp.service;

import com.example.QuizApp.dto.request.create.AnswerCreateDTO;
import com.example.QuizApp.dto.request.create.QuizCreateDTO;
import com.example.QuizApp.dto.request.update.AnswerUpdateDTO;
import com.example.QuizApp.dto.request.update.QuizUpdateDTO;
import com.example.QuizApp.dto.response.QuizDetailResponseDTO;
import com.example.QuizApp.dto.response.QuizResponseDTO;
import com.example.QuizApp.errorCode.QuizCode;
import com.example.QuizApp.errorCode.UserCode;
import com.example.QuizApp.exception.ServiceException;
import com.example.QuizApp.mapper.QuizMapper;
import com.example.QuizApp.model.Answer;
import com.example.QuizApp.model.Quiz;

import com.example.QuizApp.model.Role;
import com.example.QuizApp.model.User;
import com.example.QuizApp.repository.AnswerRepository;
import com.example.QuizApp.repository.QuizRepository;
import com.example.QuizApp.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.domain.EntityGraphUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@Slf4j
public class QuizService {
    private final QuizRepository quizRepository;
    private final AnswerRepository answerRepository;
    private final UserRepository userRepository;
    private final QuizMapper quizMapper;

    @Autowired
    public QuizService(QuizRepository quizRepository, AnswerRepository answerRepository, UserRepository userRepository, QuizMapper quizMapper) {
        this.quizRepository = quizRepository;

        this.answerRepository = answerRepository;
        this.userRepository = userRepository;
        this.quizMapper = quizMapper;
    }


    public List<QuizResponseDTO> getList() {
        return quizMapper.entityToListResponseDTO(quizRepository.findAll());
    }


    public QuizDetailResponseDTO getDetail(Long id,Principal principal) {
        Quiz quiz = quizRepository.findById(id,getQuizEntityGraph()).orElseThrow(()->new ServiceException(QuizCode.QuizNotFoundId,"Quiz with id "+id+" does not exist."));
        User user=userRepository.findByUserName(principal.getName()).orElseThrow(()->new ServiceException(UserCode.UserNotFoundUsername,"Wrong username"));
        if(!checkUserHasRole(user,"ROLE_ADMIN")&&quiz.getUser()!=user){
            throw new ServiceException(QuizCode.QuizForbidden,"You dont have permission to edit this question");
        }
        return quizMapper.entityToDetailResponseDTO(quiz);
    }
    EntityGraph getQuizEntityGraph() {
        return EntityGraphUtils.fromAttributePaths(
                Quiz.Fields.answers
        );
    }
    @Transactional
    public QuizResponseDTO createQuiz(QuizCreateDTO quizCreateDTO, Principal principal) {
        Optional<Quiz> quizOptional=quizRepository.findQuizByQuestion(quizCreateDTO.getQuestion());
        if(quizOptional.isPresent()){
            throw new ServiceException(QuizCode.QuizFoundQuestion,"Quiz with question "+quizCreateDTO.getQuestion()+" exists.");
        }
        Quiz quiz = quizMapper.DTOToEntity(quizCreateDTO);
        User user=userRepository.findByUserName(principal.getName()).orElseThrow(()->new ServiceException(UserCode.UserNotFoundUsername,"Wrong username"));
        quiz.setUser(user);
        quizRepository.save(quiz);
        for(AnswerCreateDTO answerCreateDTO : quizCreateDTO.getAnswers()){
            Answer answer = Answer.builder().answer(answerCreateDTO.getAnswer()).quiz(quiz).build();
            answerRepository.save(answer);
        }

        return quizMapper.entityToResponseDTO(quiz);
    }
    @Transactional
    public void updateQuiz(Long id, QuizUpdateDTO quizUpdateDTO, Principal principal) {
        Quiz quiz = quizRepository.findById(id).orElseThrow(()->new ServiceException(QuizCode.QuizNotFoundId,"Quiz with id "+id+" does not exist."));
        User user=userRepository.findByUserName(principal.getName()).orElseThrow(()->new ServiceException(UserCode.UserNotFoundUsername,"Wrong username"));
        if(!checkUserHasRole(user,"ROLE_ADMIN")&&quiz.getUser()!=user){
            throw new ServiceException(QuizCode.QuizForbidden,"You dont have permission to edit this question");
        }
        quizMapper.mergeQuiz(quiz,quizUpdateDTO);
        quizRepository.save(quiz);
    }
    @Transactional
    public void deleteQuiz(Long id,Principal principal) {
        Quiz quiz = quizRepository.findById(id).orElseThrow(()->new ServiceException(QuizCode.QuizNotFoundId,"Quiz with id "+id+" does not exist."));
        User user=userRepository.findByUserName(principal.getName()).orElseThrow(()->new ServiceException(UserCode.UserNotFoundUsername,"Wrong username"));
        if(!checkUserHasRole(user,"ROLE_ADMIN")&&quiz.getUser()!=user){
            throw new ServiceException(QuizCode.QuizForbidden,"You dont have permission to delete this question");
        }
        quizRepository.deleteById(id);
    }
    @Transactional
    public void updateAnswer(Long quizId, Long answerId, AnswerUpdateDTO answerUpdateDTO,Principal principal) {
        Answer answer = answerRepository.findByQuizIdAndId(quizId, answerId).orElseThrow(() -> new ServiceException(QuizCode.AnswerNotFoundId,"Answer with quizId " + quizId + " and id " + answerId + " does not exist"));
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(()->new ServiceException(QuizCode.QuizNotFoundId,"Quiz with id "+quizId+" does not exist."));
        User user=userRepository.findByUserName(principal.getName()).orElseThrow(()->new ServiceException(UserCode.UserNotFoundUsername,"Wrong username"));

        if(!checkUserHasRole(user,"ROLE_ADMIN")&&quiz.getUser()!=user){
            throw new ServiceException(QuizCode.QuizForbidden,"You dont have permission to edit this question");
        }
        quizMapper.mergeAnswer(answer,answerUpdateDTO);
        answerRepository.save(answer);
    }
    public boolean checkUserHasRole(User user,String role){
        return user.getRoles().stream().map(Role::getName).toList().contains(role);
    }

    public List<QuizResponseDTO> getListUser(Principal principal) {
        User user=userRepository.findByUserName(principal.getName()).orElseThrow(()->new ServiceException(UserCode.UserNotFoundUsername,"Wrong username"));
        return quizMapper.entityToListResponseDTO(user.getQuizzes().stream().toList());
    }

    public List<QuizResponseDTO> getListUserWithId(Long id) {
        User user=userRepository.findById(id).orElseThrow(()-> new ServiceException(UserCode.UserNotFoundId,"User with id: "+id+"does not exist"));
        return quizMapper.entityToListResponseDTO(user.getQuizzes().stream().toList());
    }
}
