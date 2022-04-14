package capston.cau.service;

import capston.cau.domain.Member;
import capston.cau.domain.board.Comment;
import capston.cau.domain.board.Post;
import capston.cau.dto.board.CommentRequestDto;
import capston.cau.exception.MemberNotFoundException;
import capston.cau.exception.PostNotFoundException;
import capston.cau.repository.CommentRepository;
import capston.cau.repository.MemberRepository;
import capston.cau.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;


@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final EntityManager em;

    @Transactional
    public Long addComment(Long memberId, Long postId, CommentRequestDto commentDto){
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);

        Comment comment = Comment.builder()
                            .post(post)
                            .member(member)
                            .comment(commentDto.getContent())
                            .build();

        return commentRepository.save(comment).getId();
    }

    @Transactional
    public Long updateComment(Long memberId, CommentRequestDto commentUpdateRequestDto){
        Comment comment = commentRepository.findById(commentUpdateRequestDto.getCommentId()).orElse(null);
        if(comment == null){
            return -1L;
        }

        if(comment.getMember().getId() != memberId){
            throw new AccessDeniedException("권한이 필요합니다.");
        }
        comment.update(comment.getComment());
        return comment.getId();
    }


//    public List<Comment> findByPostId(Long postId){
//        return commentRepository.findByPostId(postId);
//    }

}
