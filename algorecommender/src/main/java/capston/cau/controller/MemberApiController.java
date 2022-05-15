package capston.cau.controller;

import capston.cau.domain.Member;
import capston.cau.dto.SingleResult;
import capston.cau.dto.member.MemberDto;
import capston.cau.dto.member.request.MemberInfoInitRequestDto;
import capston.cau.dto.problem.ProblemStatusChangeRequest;
import capston.cau.jwt.JwtTokenProvider;
import capston.cau.service.MemberService;
import capston.cau.service.ResponseService;
import capston.cau.service.SignService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberApiController {

    private final MemberService memberService;
    private final SignService signService;
    private final ResponseService responseService;
    private final JwtTokenProvider jwtTokenProvider;

//    @GetMapping("/me")
//    public SingleResult<MemberDto> getMyInfo(HttpServletRequest request){
//        String token = jwtTokenProvider.resolveToken(request);
//        MemberDto memberInfo = memberService.getMemberProblemList(token);
//        return responseService.getSingleResult(memberInfo);
//    }

    @PostMapping("/me/init")
    public SingleResult<Long> initMemberInfo(HttpServletRequest request, @RequestBody MemberInfoInitRequestDto requestDto){
        String token = jwtTokenProvider.resolveToken(request);
        Member memberByToken = signService.findMemberByToken(token);
        Long id = memberService.initMemberInfo(memberByToken.getId(), requestDto);
        return responseService.getSingleResult(id);
    }

    @PostMapping("/me/add/{problemId}")
    public SingleResult addTryingProblem(@PathVariable Long problemId,HttpServletRequest request){
        String token = jwtTokenProvider.resolveToken(request);
        Member memberByToken = signService.findMemberByToken(token);
        Long result = memberService.addTryingProblem(memberByToken.getId(), problemId);
        return responseService.getSingleResult(null);
    }

    @PatchMapping("/me/problemStatus")
    public SingleResult changeProblemStatus(@RequestBody @Valid ProblemStatusChangeRequest requestData, HttpServletRequest request){
        String token = jwtTokenProvider.resolveToken(request);
        Member memberByToken = signService.findMemberByToken(token);
        Long result = memberService.changeProblemState(memberByToken.getId(), requestData.getProblemId(), requestData.getProblemStatus());
        return responseService.getSingleResult(null);
    }


    //TODO token 선처리를 controller단에서 해주기
}
