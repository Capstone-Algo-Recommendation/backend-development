package capston.cau.repository;

import capston.cau.domain.Problem;
import capston.cau.domain.ProblemStatus;
import capston.cau.domain.QProblem;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static capston.cau.domain.QMember.member;
import static capston.cau.domain.QMemberProblem.memberProblem;
import static capston.cau.domain.QProblem.problem;

@Repository
@RequiredArgsConstructor
public class CustomMemberRepository {

    private final EntityManager em;
    private JPAQueryFactory queryFactory;

    public List<Problem> getMemberProblemList(Long memberId){
        queryFactory = new JPAQueryFactory(em);
        List<Problem> fetch = queryFactory.selectFrom(problem)
                .join(problem.memberRelay,memberProblem)
                .fetchJoin()
                .join(memberProblem.member, member)
                .where(member.id.eq(memberId))
                .fetchJoin()
                .fetch();
        return fetch;
    }

    public List<Problem> solvedList(Long memberId){
        queryFactory = new JPAQueryFactory(em);
        List<Problem> fetch = queryFactory.selectFrom(problem)
                .join(problem.memberRelay,memberProblem)
                .fetchJoin()
                .join(memberProblem.member, member)
                .where(member.id.eq(memberId), memberProblem.problemStatus.eq(ProblemStatus.COMPLETE))
                .fetchJoin()
                .fetch();
        return fetch;
    }
}
