package capston.cau.repository;

import capston.cau.domain.*;
import capston.cau.domain.auth.SocialLoginType;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

import static capston.cau.domain.QMember.*;
import static capston.cau.domain.QMemberProblem.memberProblem;
import static capston.cau.domain.QProblem.problem;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    Optional<Member> findByEmailAndProvider(String email, SocialLoginType provider);
/*
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
*/


}
