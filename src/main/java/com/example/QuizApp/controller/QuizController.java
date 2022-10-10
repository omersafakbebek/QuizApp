package com.example.QuizApp.controller;

import com.example.QuizApp.dto.request.create.QuizCreateDTO;
import com.example.QuizApp.dto.request.update.AnswerUpdateDTO;
import com.example.QuizApp.dto.response.QuizDetailResponseDTO;
import com.example.QuizApp.dto.response.QuizResponseDTO;
import com.example.QuizApp.dto.request.update.QuizUpdateDTO;
import com.example.QuizApp.model.Quiz;
import com.example.QuizApp.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("quiz")
@PreAuthorize("isAuthenticated()")
public class QuizController {
    private final QuizService quizService;


    @Autowired
    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<QuizResponseDTO> getList(){
        return quizService.getList();
    }
    @GetMapping(path="/{id}")
    public QuizDetailResponseDTO getDetail(@PathVariable("id") Long id,Principal principal){
        return quizService.getDetail(id,principal);
    }
    @GetMapping
    public List<QuizResponseDTO> getListUser(Principal principal){return quizService.getListUser(principal);}
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/{userId}")
    public List<QuizResponseDTO> getListUserWithId(@PathVariable("userId") Long id){
        return quizService.getListUserWithId(id);
    }
    @PostMapping
    public QuizResponseDTO createQuiz(@RequestBody @Valid QuizCreateDTO quizCreateDTO, Principal principal){
        return quizService.createQuiz(quizCreateDTO,principal);
    }
    @PutMapping(path="/{id}")
    public void updateQuiz(@PathVariable("id") Long id,@RequestBody QuizUpdateDTO quizUpdateDTO,Principal principal){
        quizService.updateQuiz(id,quizUpdateDTO,principal);
    }
    @DeleteMapping(path="/{id}")
    public void deleteQuiz(@PathVariable("id") Long id,Principal principal){
        quizService.deleteQuiz(id,principal);
    }
    @PutMapping(path = "/{quizId}/answer/{answerId}")
    public void updateAnswer(@PathVariable("quizId") Long quizId, @PathVariable("answerId") Long answerId, @RequestBody AnswerUpdateDTO answerUpdateDTO,Principal principal) {
        quizService.updateAnswer(quizId, answerId, answerUpdateDTO,principal);
    }
}
