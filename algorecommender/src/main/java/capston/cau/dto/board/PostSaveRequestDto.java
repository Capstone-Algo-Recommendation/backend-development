package capston.cau.dto.board;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class PostSaveRequestDto {
    private String title;
    private String content;
    private Long problemId;
}
