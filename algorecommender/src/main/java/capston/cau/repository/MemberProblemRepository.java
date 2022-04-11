package capston.cau.repository;

import capston.cau.domain.*;
import capston.cau.exception.ProblemNotFoundException;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class MemberProblemRepository {

    private final EntityManager em;
    private final MemberRepository memberRepository;
    private final ProblemRepository problemRepository;
    private JPAQueryFactory queryFactory;

    public ProblemStatus getMemberProblemStatus(Long memberId, Long problemId){
        if(isRelayed(memberId,problemId)!=-1L){
            queryFactory = new JPAQueryFactory(em);
            MemberProblem mp = queryFactory.selectFrom(QMemberProblem.memberProblem)
                    .where(QMemberProblem.memberProblem.member.id.eq(memberId), QMemberProblem.memberProblem.problem.id.eq(problemId))
                    .fetchFirst();

            return mp.getProblemStatus();
        }
        return ProblemStatus.NONE;
    }

    public Long addTrying(Long memberId,Long problemId){
        MemberProblem mp = new MemberProblem();
        Member findMember = memberRepository.findById(memberId).orElse(null);
        Problem findProblem = problemRepository.findById(problemId).orElseThrow(ProblemNotFoundException::new);

        if(findMember ==null)
            return -1L;
        mp.setMember(findMember);
        mp.setProblem(findProblem);
        mp.setProblemStatus(ProblemStatus.TRYING);
        em.persist(mp);
        return mp.getId();
    }

    public Long isRelayed(Long memberId,Long problemId){
        queryFactory = new JPAQueryFactory(em);
        MemberProblem memberProblem = queryFactory.selectFrom(QMemberProblem.memberProblem)
                .where(QMemberProblem.memberProblem.member.id.eq(memberId),QMemberProblem.memberProblem.problem.id.eq(problemId))
                .fetchOne();
        if(memberProblem!=null){
            return memberProblem.getId();
        }
        return -1L;
    }

    public MemberProblem findById(Long id){
        return em.find(MemberProblem.class,id);
    }

    public Long changeProblemStatus(Long memberId,Long problemId,ProblemStatus status){
        queryFactory = new JPAQueryFactory(em);
        MemberProblem memberProblem = queryFactory.selectFrom(QMemberProblem.memberProblem)
                .where(QMemberProblem.memberProblem.member.id.eq(memberId), QMemberProblem.memberProblem.problem.id.eq(problemId))
                .fetchOne();

        if(memberProblem!=null) {
            memberProblem.setProblemStatus(status);
            return memberProblem.getId();
        }
        return -1L;
    }

}
