package capston.cau.dto.board;

import capston.cau.domain.board.Comment;
import capston.cau.domain.board.Post;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PostResponseDto {
    private Long id;
    private Long problemId;
    private String title;
    private String content;
    private String author;

    private List<CommentDto> comments=new ArrayList<>();

    public PostResponseDto(Post entity){
        this.id = entity.getId();
        if(entity.getProblem() !=null) {
            this.problemId = entity.getProblem().getId();
        }
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.author = entity.getMember().getName();

        List<Comment> comments = entity.getComments();
        if(comments==null){
            return;
        }
        for (Comment comment : comments) {
            this.comments.add(
                    CommentDto.builder()
                            .commentId(comment.getId())
                            .author(comment.getMember().getName())
                            .content(comment.getComment())
                            .build()
            );
        }
    }
}
