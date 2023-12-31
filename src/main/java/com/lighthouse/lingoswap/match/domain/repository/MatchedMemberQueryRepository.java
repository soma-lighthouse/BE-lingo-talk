package com.lighthouse.lingoswap.match.domain.repository;

import com.lighthouse.lingoswap.common.dto.SliceDto;
import com.lighthouse.lingoswap.match.domain.model.MatchedMember;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.lighthouse.lingoswap.match.domain.model.QMatchedMember.matchedMember;
import static com.lighthouse.lingoswap.member.domain.model.QMember.member;

@Repository
public class MatchedMemberQueryRepository {

    private final JPAQueryFactory queryFactory;

    public MatchedMemberQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public SliceDto<MatchedMember> findAllByFromMemberId(final Long fromMemberId, final Long nextId, int pageSize) {
        List<MatchedMember> matchedMembers = queryFactory
                .selectFrom(matchedMember)
                .join(matchedMember.toMember, member)
                .fetchJoin()
                .where(
                        matchedMember.fromMember.id.eq(fromMemberId),
                        matchedMemberIdLt(nextId)
                )
                .orderBy(matchedMember.id.desc())
                .limit(pageSize + 1L)
                .fetch();

        Long lastId = removeLastAndReturnNextIdByPageSize(matchedMembers, pageSize);
        return new SliceDto<>(matchedMembers, lastId);
    }

    private BooleanExpression matchedMemberIdLt(Long nextId) {
        return nextId == null ? null : matchedMember.id.lt(nextId);
    }

    private Long removeLastAndReturnNextIdByPageSize(final List<MatchedMember> matchedMembers, final int pageSize) {
        Long lastId = -1L;
        if (matchedMembers.size() > pageSize) {
            matchedMembers.remove(pageSize);
            lastId = matchedMembers.get(pageSize - 1).getId();
        }
        return lastId;
    }

}
