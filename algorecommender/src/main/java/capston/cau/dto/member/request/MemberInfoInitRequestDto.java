package capston.cau.dto.member.request;

import capston.cau.dto.problem.ProblemDto;
import lombok.Data;
import java.util.List;

@Data
public class MemberInfoInitRequestDto {
    String bojId;
    String name;
    List<ProblemDto> problems;
}