package capston.cau.repository;

import capston.cau.domain.*;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static capston.cau.domain.QMember.*;
import static capston.cau.domain.QMemberProblem.memberProblem;
import static capston.cau.domain.QProblem.problem;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;
    private JPAQueryFactory queryFactory;

    public Long save(Member member){
        em.persist(member);
        return member.getId();
    }

    public Optional<Member> findById(Long id){
        return Optional.ofNullable(em.find(Member.class,id));
    }

    public Optional<Member> findByEmail(String email){
        queryFactory = new JPAQueryFactory(em);
        Member findMember = queryFactory.selectFrom(QMember.member)
                .where(QMember.member.email.eq(email))
                .fetchOne();
        return Optional.ofNullable(findMember);
    }

    public Boolean existsByEmail(String email){
        queryFactory = new JPAQueryFactory(em);
        Member findMember = queryFactory.selectFrom(QMember.member)
                .where(QMember.member.email.eq(email))
                .fetchOne();
        if(findMember==null)
            return false;
        return true;
    }

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
