package capston.cau.service;

import capston.cau.domain.Member;
import capston.cau.domain.Problem;
import capston.cau.domain.ProblemStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private ProblemService problemService;

    @Autowired
    private EntityManager em;

    Long testId;

    @BeforeEach
    void before(){
        Member member1 = new Member();
        member1.setName("testMem1");
        member1.setBojId("testMem1BojId");
        memberService.join(member1);
        testId = member1.getId();

        Problem problem1 = new Problem(1000L,"A+B","www.test1000.com");
        Problem problem2 = new Problem(1001L,"A-B","www.test1001.com");
        problemService.addProblem(problem1);
        problemService.addProblem(problem2);

        memberService.addTryingProblem(testId,1000L);
        memberService.addTryingProblem(testId,1001L);
    }

    @Test
//    @Rollback(false)
    void 멤버_추가(){
        Member member1 = new Member();
        member1.setName("testMem2");
        member1.setBojId("testMem2BojId");

        memberService.join(member1);

        Member findMember = memberService.findById(member1.getId());
        assertThat(findMember.getName()).isEqualTo("testMem2");
    }

    @Test
//    @Rollback(false)
    void 멤버_도전문제_추가(){
        Problem problem = new Problem(1002L,"A*B","www.test3.com");
        problemService.addProblem(problem);
        memberService.addTryingProblem(testId,problem.getId());
        em.flush();
        em.clear();
        assertThat(memberService.getMemberProblemList(testId).size()).isEqualTo(3);
    }
    
    @Test
//    @Rollback(false)
    void 멤버_도전문제_성공(){
        List<Problem> memberSolvedProblems = memberService.getMemberSolvedProblems(testId);
        int before = memberSolvedProblems.size();
        memberService.changeProblemState(testId,1000L, ProblemStatus.COMPLETE);

        em.flush();

        List<Problem> memberSolvedProblems2 = memberService.getMemberSolvedProblems(testId);
        assertThat(before).isEqualTo(memberSolvedProblems2.size()-1);
    }
}