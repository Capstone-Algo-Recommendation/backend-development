package capston.cau.dto.board;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PostPreviewDto {
    Long id;
    String title;
    String content;
    LocalDateTime writtenAt;
}
