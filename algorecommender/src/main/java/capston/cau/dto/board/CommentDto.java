package capston.cau.dto.board;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentDto {
    Long commentId;
    String author;
    String content;
}
