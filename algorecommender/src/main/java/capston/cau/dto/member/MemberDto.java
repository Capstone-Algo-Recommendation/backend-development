package capston.cau.dto.member;

import capston.cau.dto.problem.ProblemDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MemberDto {
    private String name;
    private String bojId;
    private List<ProblemDto> problemList=new ArrayList<>();

    public void addProblemToList(ProblemDto problemDto){
        problemList.add(problemDto);
    }
}
