package capston.cau.service;

import capston.cau.domain.Member;
import capston.cau.domain.Problem;
import capston.cau.domain.ProblemStatus;
import capston.cau.dto.member.MemberDto;
import capston.cau.dto.problem.ProblemDto;
import capston.cau.exception.LoginFailureException;
import capston.cau.exception.MemberNotFoundException;
import capston.cau.jwt.dto.TokenRequestDto;
import capston.cau.repository.CustomMemberRepository;
import capston.cau.repository.MemberProblemRepository;
import capston.cau.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final CustomMemberRepository customMemberRepository;

    private final MemberProblemRepository relayRepository;
    private final SignService signService;

    @Transactional
    public Long join(Member member){
        memberRepository.save(member);
        return member.getId();
    }

    public Member findById(Long id){
        return memberRepository.findById(id).orElse(null);
    }

    //중복 제거하기
    @Transactional
    public Long addTryingProblem(Long memberId,Long problemId){
        Long isRelayed = relayRepository.isRelayed(memberId,problemId);
        if(isRelayed==-1L)
            return relayRepository.addTrying(memberId,problemId);
        else
            return isRelayed;
    }

    @Transactional
    public Long changeProblemState(Long memberId, Long problemId, ProblemStatus status){
        return relayRepository.changeProblemStatus(memberId,problemId,status);
    }

    public MemberDto getMemberProblemList(String token) {
        Member member = signService.findMemberByToken(token);
        List<Problem> memberProblemList = customMemberRepository.getMemberProblemList(member.getId());
        List<ProblemDto> problemDtos = new ArrayList<>();

        for (Problem problem : memberProblemList) {
            ProblemDto problemDto = ProblemDto.builder()
                    .id(problem.getId())
                    .name(problem.getName())
                    .url(problem.getUrl())
                    .status(relayRepository.getMemberProblemStatus(member.getId(),problem.getId()))
                    .build();
            problemDtos.add(problemDto);
        }

        MemberDto memberDto = MemberDto.builder()
                .name(member.getName())
                .email(member.getEmail())
                .bojId(member.getBojId())
                .problemList(problemDtos)
                .build();

        return memberDto;
    }

    public List<Problem> getMemberSolvedProblems(Long id){
        return customMemberRepository.solvedList(id);
    }

    public String getProblemStatus(Long memberId, Long problemId){
        ProblemStatus status = relayRepository.getMemberProblemStatus(memberId, problemId);
        return status.toString();
    }

}
