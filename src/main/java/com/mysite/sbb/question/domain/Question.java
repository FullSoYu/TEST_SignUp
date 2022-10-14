package com.mysite.sbb.question.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mysite.sbb.answer.domain.Answer;
import com.mysite.sbb.siteuser.domain.SiteUser;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 200)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;

    private LocalDateTime modifyDate;

    @OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Answer> answerList;

    @ManyToOne
    private SiteUser author;

    @ManyToMany
    Set<SiteUser> voter;
}
