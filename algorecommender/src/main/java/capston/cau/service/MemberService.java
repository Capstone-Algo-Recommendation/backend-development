package capston.cau.service;

import capston.cau.domain.Member;
import capston.cau.domain.Problem;
import capston.cau.domain.ProblemStatus;
import capston.cau.repository.MemberProblemRepository;
import capston.cau.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemberProblemRepository relayRepository;

    @Transactional
    public Long join(Member member){
        memberRepository.save(member);
        return member.getId();
    }

    public Member findById(Long id){
        return memberRepository.findById(id);
    }

    //중복 제거하기
    @Transactional
    public Long addTryingProblem(Long memberId,Long problemId){
        Long isRelayed = relayRepository.isRelayed(memberId,problemId);
        System.out.println("isRelayed = " + isRelayed);
        if(isRelayed==-1L)
            return relayRepository.addTrying(memberId,problemId);
        else
            return isRelayed;
    }

    @Transactional
    public Long changeProblemState(Long memberId, Long problemId, ProblemStatus status){
        return relayRepository.changeProblemStatus(memberId,problemId,status);
    }

    public List<Problem> getMemberProblemList(Long id) {return memberRepository.getMemberProblemList(id);}

    public List<Problem> getMemberSolvedProblems(Long id){
        return memberRepository.solvedList(id);
    }

    public String getProblemStatus(Long memberId, Long problemId){
        ProblemStatus status = relayRepository.getMemberProblemStatus(memberId, problemId);
        return status.toString();
    }

}
