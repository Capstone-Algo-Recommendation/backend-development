package capston.cau.service;

import capston.cau.domain.Member;
import capston.cau.domain.Problem;
import capston.cau.domain.ProblemCategory;
import capston.cau.domain.ProblemStatus;
import capston.cau.domain.auth.Role;
import capston.cau.dto.member.MemberDto;
import capston.cau.dto.member.request.MemberInfoInitRequestDto;
import capston.cau.dto.problem.ProblemDto;
import capston.cau.exception.MemberNotFoundException;
import capston.cau.repository.CustomMemberRepository;
import capston.cau.repository.MemberProblemRepository;
import capston.cau.repository.MemberRepository;
import capston.cau.repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import capston.cau.jwt.dto.TokenRequestDto;
import capston.cau.exception.LoginFailureException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final CustomMemberRepository customMemberRepository;
    private final ProblemRepository problemRepository;

    private final MemberProblemRepository relayRepository;
    private final SignService signService;

    @Transactional
    public Long join(Member member){
        Member isExist = memberRepository.findByEmail(member.getEmail()).orElse(null);
        if(isExist!=null)
            return isExist.getId();
        memberRepository.save(member);
        return member.getId();
    }

    public Member findById(Long id){
        return memberRepository.findById(id).orElse(null);
    }

    //중복 제거하기
    @Transactional
    public Long addTryingProblem(Long memberId,Long problemId,ProblemStatus status){
        Long isRelayed = relayRepository.isRelayed(memberId,problemId);
        if(isRelayed==-1L)
            return relayRepository.addTrying(memberId,problemId,status);
        else
            return isRelayed;
    }

    @Transactional
    public Long changeProblemState(Long memberId, Long problemId, ProblemStatus status){
        return relayRepository.changeProblemStatus(memberId,problemId,status);
    }

    @Transactional
    public Long addMemoToProblem(Long memberId, Long problemId, String memo){
        return relayRepository.addMemo(memberId,problemId,memo);
    }

    public MemberDto getMemberProblemList(Long id) {
        Member member = this.findById(id);
        List<Problem> memberProblemList = customMemberRepository.getMemberProblemList(id);
        List<ProblemDto> problemDtos = new ArrayList<>();

        for (Problem problem : memberProblemList) {

            List<String> categories = problemRepository.findProblemCategory(problem.getId());

            ProblemDto problemDto = ProblemDto.builder()
                    .id(problem.getId())
                    .name(problem.getName())
                    .url(problem.getUrl())
                    .level(problem.getLevel())
                    .status(relayRepository.getMemberProblemStatus(id,problem.getId()))
                    .categories(categories)
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

    @Transactional
    public Long initMemberInfo(Long memberId, MemberInfoInitRequestDto requestDto){
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
        if(member.getRole().equals(Role.ROLE_GUEST)) {
            member.setBojId(requestDto.getBojId());
            if(requestDto.getProblems()!=null) {
                for (ProblemDto problem : requestDto.getProblems()) {
                    relayRepository.addProblemInit(memberId, problem.getId(), problem.getStatus());
                }
            }
            member.setName(requestDto.getName());
            member.setRole(Role.ROLE_MEMBER);
        }else{
            if(requestDto.getProblems()!=null) {
                for (ProblemDto problem : requestDto.getProblems()) {
                    if(relayRepository.isRelayed(memberId,problem.getId())==-1L)
                        relayRepository.addProblemInit(memberId, problem.getId(), problem.getStatus());
                }
            }
        }
        return member.getId();
    }

    @Transactional
    public void withdrawl(Long memberId){
        memberRepository.delete(memberRepository.getById(memberId));
        return ;
    }

    public List<Problem> getMemberSolvedProblems(Long id){
        return customMemberRepository.solvedList(id);
    }

    public String getProblemStatus(Long memberId, Long problemId){
        ProblemStatus status = relayRepository.getMemberProblemStatus(memberId, problemId);
        return status.toString();
    }

}
