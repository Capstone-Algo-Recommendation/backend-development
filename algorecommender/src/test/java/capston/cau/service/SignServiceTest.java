package capston.cau.service;

import capston.cau.domain.Member;
import capston.cau.domain.Problem;
import capston.cau.domain.ProblemStatus;
import capston.cau.dto.member.request.MemberLoginRequestDto;
import capston.cau.dto.member.request.MemberRegisterRequestDto;
import capston.cau.dto.member.response.MemberLoginResponseDto;
import capston.cau.jwt.dto.TokenRequestDto;
import capston.cau.repository.CustomMemberRepository;
import capston.cau.repository.MemberProblemRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
class SignServiceTest {

    @Autowired
    private SignService signService;

    @Autowired
    private CustomMemberRepository customMemberRepository;

    @Autowired
    private MemberProblemRepository memberProblemRepository;

    @Autowired
    private ProblemService problemService;

    @Test
//    @Rollback(false)
    void 회원추가(){
        MemberRegisterRequestDto dto = new MemberRegisterRequestDto();
        dto.setEmail("bounce1011@naver.com");
        dto.setPassword("111");
        signService.registerMember(dto);

        MemberLoginResponseDto memberLoginResponseDto = signService.loginMember(new MemberLoginRequestDto("bounce1011@naver.com", "111"));
        System.out.println("memberLoginResponseDto = " + memberLoginResponseDto);

        TokenRequestDto tokenRequestDto = new TokenRequestDto(memberLoginResponseDto.getToken(), memberLoginResponseDto.getRefreshToken());
        Member findMember = signService.findMemberByToken(tokenRequestDto.getAccessToken());

        problemService.addProblem(new Problem(1000L,"1+1","www.1+1.com"));
        problemService.addProblem(new Problem(1001L,"1-1","www.1-1.com"));

        memberProblemRepository.addTrying(findMember.getId(),1000L);
        memberProblemRepository.addTrying(findMember.getId(),1001L);

        List<Problem> memberProblemList = customMemberRepository.getMemberProblemList(findMember.getId());
        for (Problem problem : memberProblemList) {
            System.out.println("problem = " + problem.getName());
        }

        memberProblemRepository.changeProblemStatus(findMember.getId(),1000L, ProblemStatus.COMPLETE);

        List<Problem> problems = customMemberRepository.solvedList(findMember.getId());
        for (Problem problem : problems) {
            System.out.println("problem = " + problem.getId());
        }
//        Assertions.assertThat(findMember.getEmail()).isEqualTo("bounce1011@naver.com");
    }

}















