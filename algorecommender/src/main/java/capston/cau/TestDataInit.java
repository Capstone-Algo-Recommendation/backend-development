package capston.cau;

import capston.cau.domain.Member;
import capston.cau.domain.Problem;
import capston.cau.domain.auth.Role;
import capston.cau.domain.auth.SocialLoginType;
import capston.cau.dto.board.PostSaveRequestDto;
import capston.cau.repository.MemberRepository;
import capston.cau.repository.ProblemRepository;
import capston.cau.service.MemberService;
import capston.cau.service.PostService;
import capston.cau.service.ProblemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class TestDataInit {

    private final MemberService memberService;
    private final ProblemService problemService;
    private final PostService postService;

    @PostConstruct
    public void init(){

        Problem problem1 = new Problem(1000L,"A+B","www.test1000.com");
        Problem problem2 = new Problem(1001L,"A-B","www.test1001.com");
        Problem problem3 = new Problem(1002L,"A*B","www.test1002.com");
        Problem problem4 = new Problem(1003L,"A/B","www.test1003.com");
        Problem problem5 = new Problem(1004L,"prob5","www.test1004.com");
        Problem problem6 = new Problem(1005L,"prob6","www.test1005.com");
        Problem problem7 = new Problem(1006L,"prob7","www.test1006.com");
        Problem problem8 = new Problem(1007L,"prob8","www.test1007.com");
        Problem problem9 = new Problem(1008L,"prob9","www.test1008.com");
        Problem problem10 = new Problem(1009L,"prob10","www.test1009.com");

        problemService.addProblem(problem1);
        problemService.addProblem(problem2);
        problemService.addProblem(problem3);
        problemService.addProblem(problem4);
        problemService.addProblem(problem5);
        problemService.addProblem(problem6);
        problemService.addProblem(problem7);
        problemService.addProblem(problem8);
        problemService.addProblem(problem9);
        problemService.addProblem(problem10);

        Member member = Member.builder()
                .email("test")
                .password("asegsrbasrbasrbas")
                .role(Role.ROLE_MEMBER)
                .provider(SocialLoginType.GOOGLE)
                .build();

        member.setName("helloworld~");
        Long id = memberService.join(member);

        for(int i=0;i<30;i++) {
            PostSaveRequestDto postSaveRequestDto = new PostSaveRequestDto();
            postSaveRequestDto.setTitle("test title"+String.valueOf(i));
            postSaveRequestDto.setContent("test content"+String.valueOf(i));
            postSaveRequestDto.setProblemId(1000L);
            postService.save(id,postSaveRequestDto);
        }
    }
}
