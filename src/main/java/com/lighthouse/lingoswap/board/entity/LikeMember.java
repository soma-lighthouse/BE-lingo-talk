package com.lighthouse.lingoswap.board.entity;

import com.lighthouse.lingoswap.common.entity.BaseEntity;
import com.lighthouse.lingoswap.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LikeMember extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Question question;

    private Boolean isValid;

    private LikeMember(Member member, Question question) {
        this.member = member;
        this.question = question;
        this.isValid = Boolean.FALSE;
    }

    public static LikeMember of(Member member, Question question) {
        return new LikeMember(member, question);
    }

}