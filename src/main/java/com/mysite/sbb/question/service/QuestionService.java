package com.mysite.sbb.question.service;

import com.mysite.sbb.question.dao.QuestionRepository;
import com.mysite.sbb.question.domain.Question;
import com.mysite.sbb.siteuser.domain.SiteUser;
import com.mysite.sbb.util.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;

    public List<Question> getList() {
        return questionRepository.findAll();
    }

    public Question getQuestion(Integer id) {
        return questionRepository.findById(id).orElseThrow(() -> new DataNotFoundException("question not found"));
    }

    public void create(String subject, String content, SiteUser siteUser) {
        Question q = new Question();
        q.setSubject(subject);
        q.setContent(content);
        q.setCreateDate(LocalDateTime.now());
        q.setAuthor(siteUser);
        questionRepository.save(q);
    }

    public Page<Question> getList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.questionRepository.findAll(pageable);
    }

    public void modify(Question question, String subject, String content) {
        question.setSubject(subject);
        question.setContent(content);
        question.setModifyDate(LocalDateTime.now());
        questionRepository.save(question);
    }

    public void delete(Question question) {
        questionRepository.delete(question);
    }

    public void vote(Question question, SiteUser siteUser) {

        if(question.getVoter().contains(siteUser)) {
            question.getVoter().remove(siteUser);
        } else {
            question.getVoter().add(siteUser);
        }
        this.questionRepository.save(question);

    }
}
