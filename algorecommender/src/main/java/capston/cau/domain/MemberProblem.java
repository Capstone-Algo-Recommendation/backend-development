package capston.cau.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class MemberProblem {

    @Id @GeneratedValue
    @Column(name = "member_problem_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="problem_id")
    private Problem problem;

    @Enumerated(EnumType.STRING)
    private ProblemStatus problemStatus;

    public void setMember(Member member) {
        this.member = member;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    public void setProblemStatus(ProblemStatus problemStatus) {
        this.problemStatus = problemStatus;
    }
}
