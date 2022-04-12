package capston.cau.dto.board;

import capston.cau.domain.board.Comment;
import capston.cau.domain.board.Post;
import lombok.Data;

import java.util.List;

@Data
public class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private String author;

    private List<CommentDto> comments;

    public PostResponseDto(Post entity){
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.author = entity.getMember().getName();

        entity.getComments();
    }
}
