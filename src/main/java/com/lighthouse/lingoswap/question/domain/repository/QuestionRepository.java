package com.lighthouse.lingoswap.question.domain.repository;

import com.lighthouse.lingoswap.member.domain.model.Member;
import com.lighthouse.lingoswap.question.domain.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    @Query("select q from Question q join fetch q.questionCategory.category where q.questionCreatedMember.member = :member order by q.id desc")
    List<Question> findByCreatedMember(@Param("member") Member member);

}