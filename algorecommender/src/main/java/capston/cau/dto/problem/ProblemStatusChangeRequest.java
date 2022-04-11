package capston.cau.dto.problem;

import capston.cau.domain.ProblemStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProblemStatusChangeRequest {

    @NotNull(message = "Please enter problem id")
    private Long problemId;

    private ProblemStatus problemStatus;
}
