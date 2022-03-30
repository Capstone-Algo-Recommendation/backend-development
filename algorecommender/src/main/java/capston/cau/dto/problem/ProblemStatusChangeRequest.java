package capston.cau.dto.problem;

import capston.cau.domain.ProblemStatus;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class ProblemStatusChangeRequest {

    @NotNull(message = "Please enter member id")
    private Long memberId;

    @NotNull(message = "Please enter problem id")
    private Long problemId;

    private ProblemStatus problemStatus;
}
