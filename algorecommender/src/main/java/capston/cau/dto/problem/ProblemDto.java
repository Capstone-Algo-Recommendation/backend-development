package capston.cau.dto.problem;

import capston.cau.domain.ProblemStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProblemDto {

    private Long id;
    private String name;
    private String url;
    private ProblemStatus status;

    @Builder
    public ProblemDto(Long id, String name, String url, ProblemStatus status) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.status = status;
    }
}
