package com.mysite.sbb.question.dao;

import com.mysite.sbb.question.QuestionVoterInterface;
import com.mysite.sbb.question.domain.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface QuestionRepository extends JpaRepository<Question, Integer> {
    Question findBySubject(String s);

    List<Question> findAllBySubject(String subject);

    List<Question> findAllBySubjectIn(List<String> searchWordList);

    List<Question> findBySubjectAndContent(String subject, String content);

    List<Question> findBySubjectLike(String searchString);

    Page<Question> findAll(Pageable pageable);

    @Query(value = "SELECT * FROM question_voter WHERE voter_id = ?1 AND question_id = ?2", nativeQuery = true)
    QuestionVoterInterface findQuestionByVoter(Long userId, Long questionId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM question_voter WHERE voter_id = ?1 AND question_id = ?2", nativeQuery = true)
    void deleteQuestionByVoter(Long userId, long questionId);

}
