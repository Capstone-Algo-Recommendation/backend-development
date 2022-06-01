package capston.cau.dto.board;

import lombok.Data;

@Data
public class PostSaveRequestDto {
    private String title;
    private String content;
    private Long problemId;
}
