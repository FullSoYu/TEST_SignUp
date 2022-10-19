package com.mysite.sbb.question.dao;

import com.mysite.sbb.question.domain.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Integer> {
    Question findBySubject(String s);

    List<Question> findAllBySubject(String subject);

    List<Question> findAllBySubjectIn(List<String> searchWordList);

    List<Question> findBySubjectAndContent(String subject, String content);

    List<Question> findBySubjectLike(String searchString);

    Page<Question> findAll(Pageable pageable);

    Page<Question> findAll(Specification<Question> spec, Pageable pageable);
}
