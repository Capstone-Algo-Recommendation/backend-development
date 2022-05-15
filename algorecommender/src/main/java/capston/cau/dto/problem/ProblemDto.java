package capston.cau.dto.problem;

import capston.cau.domain.ProblemStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ProblemDto {

    private Long id;
    private String name;
    private String url;
    private ProblemStatus status;
    private Long level;
    private List<String> categories;

    @Builder
    public ProblemDto(Long id, String name, String url, Long level,ProblemStatus status,List<String> categories) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.url = url;
        this.status = status;
        this.categories = categories;
    }
}
