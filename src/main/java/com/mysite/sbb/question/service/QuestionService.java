package com.mysite.sbb.question.service;

import com.mysite.sbb.answer.domain.Answer;
import com.mysite.sbb.question.dao.QuestionRepository;
import com.mysite.sbb.question.domain.Question;
import com.mysite.sbb.siteuser.domain.SiteUser;
import com.mysite.sbb.siteuser.service.UserService;
import com.mysite.sbb.util.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final UserService userService;
    private final QuestionRepository questionRepository;

    private final EntityManager em;

    private Specification<Question> search(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Question> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);  // 중복을 제거
                Join<Question, SiteUser> u1 = q.join("author", JoinType.LEFT);
                Join<Question, Answer> a = q.join("answerList", JoinType.LEFT);
                Join<Answer, SiteUser> u2 = a.join("author", JoinType.LEFT);
                return cb.or(cb.like(q.get("subject"), "%" + kw + "%"), // 제목
                        cb.like(q.get("content"), "%" + kw + "%"),      // 내용
                        cb.like(u1.get("username"), "%" + kw + "%"),    // 질문 작성자
                        cb.like(a.get("content"), "%" + kw + "%"),      // 답변 내용
                        cb.like(u2.get("username"), "%" + kw + "%"));   // 답변 작성자
            }
        };
    }

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

    public Page<Question> getList(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        Specification<Question> spec = search(kw);
        return this.questionRepository.findAll(spec, pageable);
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
        System.out.println(question.getVoter());
        System.out.println("@AuthenticationPrincipal: " + siteUser);
        System.out.println("from userRepository: " + userService.getUser(siteUser.getUsername()));
        System.out.println("em: " + em.find(SiteUser.class, siteUser.getId()));
        if(!question.getVoter().contains(em.find(SiteUser.class, siteUser.getId()))) {
            question.getVoter().add(em.find(SiteUser.class, siteUser.getId()));
        } else {
            question.getVoter().remove(em.find(SiteUser.class, siteUser.getId()));
        }
        questionRepository.save(question);

    }
}
