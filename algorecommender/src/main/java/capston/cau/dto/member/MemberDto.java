package capston.cau.dto.member;

import capston.cau.dto.problem.ProblemDto;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
public class MemberDto {
    private String name;
    private String email;
    private String bojId;
    private List<ProblemDto> problemList=new ArrayList<>();

    @Builder
    public MemberDto(String name, String email,String bojId, List<ProblemDto> problemList){
        this.name = name;
        this.email = email;
        this.bojId = bojId;
        this.problemList = problemList;
    }

    public void addProblemToList(ProblemDto problemDto){
        problemList.add(problemDto);
    }
}
