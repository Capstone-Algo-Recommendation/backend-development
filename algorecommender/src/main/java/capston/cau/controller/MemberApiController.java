package capston.cau.controller;

import capston.cau.domain.Member;
import capston.cau.domain.Problem;
import capston.cau.domain.ProblemStatus;
import capston.cau.dto.member.CreateMemberRequest;
import capston.cau.dto.member.MemberDto;
import capston.cau.dto.problem.ProblemDto;
import capston.cau.dto.problem.ProblemStatusChangeRequest;
import capston.cau.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/api/member/create")
    public ResponseEntity<Long> createMember(@RequestBody @Valid CreateMemberRequest request){
        Member member = new Member();
        member.setName(request.getName());
        memberService.join(member);
        return new ResponseEntity<>(member.getId(), HttpStatus.OK);
    }

    @GetMapping("/api/member/{id}")
    public MemberDto findMember(@PathVariable("id") Long id){
        Member member = memberService.findById(id);

        MemberDto memberDto = new MemberDto();
        memberDto.setName(member.getName());
        memberDto.setBojId(member.getBojId());

        List<Problem> memberProblemList = memberService.getMemberProblemList(id);

        for (Problem problem : memberProblemList) {
            ProblemDto problemDto = new ProblemDto();
            problemDto.setId(problem.getId());
            problemDto.setName(problem.getName());
            problemDto.setUrl(problem.getUrl());
            problemDto.setStatus(memberService.getProblemStatus(member.getId(),problem.getId()));
            memberDto.addProblemToList(problemDto);
        }
        return memberDto;
    }

    @PostMapping("/api/member/{memberId}/problem/{problemId}")
    public String addTryingProblem(@PathVariable Long memberId,@PathVariable Long problemId){
        Long result = memberService.addTryingProblem(memberId, problemId);
        return "ok";
    }

    @PatchMapping("/api/member/problem/status")
    public ResponseEntity<Long> changeProblemStatus(@RequestBody @Valid ProblemStatusChangeRequest request){
        Long result = memberService.changeProblemState(request.getMemberId(), request.getProblemId(), request.getProblemStatus());
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    /**
     *
    @GetMapping("/api/member/{id}/solved")
    public List<ProblemDto> getSolvedProblem(@PathVariable Long id){
        List<Problem> memberSolvedProblems = memberService.getMemberSolvedProblems(id);
        List<ProblemDto> solvedProblemDto = new ArrayList<>();
        for (Problem problem : memberSolvedProblems) {
            ProblemDto problemDto = new ProblemDto();
            problemDto.setId(problem.getId());
            problemDto.setName(problem.getName());
            problemDto.setUrl(problem.getUrl());
            problemDto.setStatus(ProblemStatus.COMPLETE.toString());
            solvedProblemDto.add(problemDto);
        }
        return solvedProblemDto;
    }
     */

}
