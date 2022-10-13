package com.mysite.sbb.answer.service;

import com.mysite.sbb.answer.dao.AnswerRepository;
import com.mysite.sbb.answer.domain.Answer;
import com.mysite.sbb.question.domain.Question;
import com.mysite.sbb.siteuser.domain.SiteUser;
import com.mysite.sbb.util.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;
    public void create(Question question, String content, SiteUser siteUser) {
        Answer answer = new Answer();
        answer.setCreateDate(LocalDateTime.now());
        answer.setQuestion(question);
        answer.setContent(content);
        answer.setAuthor(siteUser);
        answerRepository.save(answer);
    }

    public Answer getAnswer(Integer id) {
        Optional<Answer> answer = this.answerRepository.findById(id);
        return answer.orElseThrow(() -> new DataNotFoundException("answer not found"));
    }

    public void modify(Answer answer, String content) {
        answer.setContent(content);
        answer.setModifyDate(LocalDateTime.now());
        this.answerRepository.save(answer);
    }


}
