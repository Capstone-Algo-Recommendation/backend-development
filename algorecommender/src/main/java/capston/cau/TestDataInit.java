package capston.cau;

import capston.cau.domain.Member;
import capston.cau.domain.Problem;
import capston.cau.repository.MemberRepository;
import capston.cau.repository.ProblemRepository;
import capston.cau.service.MemberService;
import capston.cau.service.ProblemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@RequiredArgsConstructor
public class TestDataInit {

    private final MemberService memberService;
    private final ProblemService problemService;

    @PostConstruct
    public void init(){

//        Member member1 = new Member();
//        member1.setName("testMem1");
//        member1.setBojId("testMem1BojId");
//        memberService.join(member1);
//
//        Problem problem1 = new Problem(1000L,"A+B","www.test1000.com");
//        Problem problem2 = new Problem(1001L,"A-B","www.test1001.com");
//
//        problemService.addProblem(problem1);
//        problemService.addProblem(problem2);
//
//        memberService.addTryingProblem(member1.getId(),problem1.getId());
//        memberService.addTryingProblem(member1.getId(),problem2.getId());
    }

}
