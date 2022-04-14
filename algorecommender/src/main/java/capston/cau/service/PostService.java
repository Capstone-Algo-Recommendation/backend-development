package capston.cau.service;

import capston.cau.domain.Member;
import capston.cau.domain.Problem;
import capston.cau.domain.board.Comment;
import capston.cau.domain.board.Post;
import capston.cau.dto.board.CommentRequestDto;
import capston.cau.dto.board.PostResponseDto;
import capston.cau.dto.board.PostSaveRequestDto;
import capston.cau.dto.board.PostUpdateRequestDto;
import capston.cau.exception.MemberNotFoundException;
import capston.cau.exception.PostNotFoundException;
import capston.cau.repository.CommentRepository;
import capston.cau.repository.MemberRepository;
import capston.cau.repository.PostRepository;
import capston.cau.repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Optional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final ProblemRepository problemRepository;
    private final EntityManager em;

//    public List<>

    @Transactional
    public Long save(Long memberId, PostSaveRequestDto saveRequestDto){
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
        Problem problem = problemRepository.findById(saveRequestDto.getProblemId()).orElse(null);
        Post post = Post.builder()
                        .member(member)
                        .problem(problem)
                        .title(saveRequestDto.getTitle())
                        .content(saveRequestDto.getContent())
                        .build();

        return postRepository.save(post).getId();
    }

    @Transactional
    public Long update(Long postId, Long memberId, PostUpdateRequestDto updateRequestDto){
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);

        if(member.getId() != post.getMember().getId()){
            throw new AccessDeniedException("권한이 필요합니다.");
        }
        post.update(updateRequestDto.getTitle(), updateRequestDto.getContent());
        return postId;
    }

    @Transactional
    public void delete(Long postId,Long memberId){
        Member member = memberRepository.findById(memberId).orElseThrow(MemberNotFoundException::new);
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);

        if(member.getId() != post.getMember().getId()){
            throw new AccessDeniedException("권한이 필요합니다.");
        }
        postRepository.delete(post);
    }

    public PostResponseDto findById(Long id){
        Post post = postRepository.findByPostIdWithComment(id).orElse(null);
        if(post==null){
            post = postRepository.findById(id).get();
        }
        return new PostResponseDto(post);
    }

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
    public Long updateComment(Long memberId, Long postId,CommentRequestDto commentUpdateRequestDto){
        Post post = postRepository.findByPostIdWithComment(postId).orElseThrow(PostNotFoundException::new);
        Comment comment = commentRepository.findById(commentUpdateRequestDto.getCommentId()).orElse(null);
        if(comment == null){
            return -1L;
        }

        if(comment.getMember().getId() != memberId){
            throw new AccessDeniedException("권한이 필요합니다.");
        }

        post.updateComment(commentUpdateRequestDto.getCommentId(), commentUpdateRequestDto.getContent());
        return comment.getId();
    }

    @Transactional
    public void deleteComment(Long memberId,Long commentId){
        Comment comment = commentRepository.findById(commentId).orElse(null);
        if(comment.getMember().getId() != memberId){
            throw new AccessDeniedException("권한이 필요합니다.");
        }
        comment.getPost().getComments().remove(comment);
        commentRepository.delete(comment);
    }
}
