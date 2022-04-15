package capston.cau.dto.board;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostPreviewDto {
    Long id;
    String title;
    String content;
}
