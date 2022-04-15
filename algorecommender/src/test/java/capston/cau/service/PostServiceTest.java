//package capston.cau.service;
//
//import capston.cau.domain.Member;
//import capston.cau.domain.auth.Role;
//import capston.cau.domain.auth.SocialLoginType;
//import capston.cau.dto.board.*;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.core.parameters.P;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.persistence.EntityManager;
//import java.security.Provider;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@Transactional
//class PostServiceTest {
//
//    @Autowired
//    private PostService postService;
//
//    @Autowired
//    private MemberService memberService;
//
//    @Autowired
//    private CommentService commentService;
//
//    @Autowired
//    private EntityManager em;
//
//    @Test
//    void 글_작성(){
//
//        Member member = Member.builder()
//                .email("test")
//                .password("asegsrbasrbasrbas")
//                .role(Role.ROLE_MEMBER)
//                .provider(SocialLoginType.GOOGLE)
//                .build();
//
//        Long id = memberService.join(member);
//
//        PostSaveRequestDto postSaveRequestDto = new PostSaveRequestDto();
//        postSaveRequestDto.setTitle("test title");
//        postSaveRequestDto.setContent("test content");
//        postSaveRequestDto.setProblemId(1000L);
//
//        Long postId = postService.save(id,postSaveRequestDto);
////        PostResponseDto findPost = postService.findById(postId);
//        PostResponseDto findPost = postService.findById(postId);
//
//        em.flush();
//        em.clear();
//
//        for (int i=0 ;i<10;i++ ) {
//            CommentRequestDto commentRequestDto = new CommentRequestDto();
//            commentRequestDto.setContent("hello!"+ String.valueOf(i));
//            postService.addComment(id,postId,commentRequestDto);
//        }
//
////        PostResponseDto findPost = postService.findById(postId);
//        System.out.println("findPost.getId() = " + findPost.getId());
//
//        postService.deleteComment(id,7L);
//
//        CommentRequestDto commentRequestDto = new CommentRequestDto();
//        commentRequestDto.setCommentId(4L);
//
//        commentRequestDto.setContent("update Test");
//        Long aLong = postService.updateComment(1L, postId,commentRequestDto);
//
//        System.out.println("aLong = " + aLong);
//        em.flush();
//        em.clear();
//
//        PostUpdateRequestDto postUpdateRequestDto = new PostUpdateRequestDto();
//        postUpdateRequestDto.setTitle("update title");
//        postUpdateRequestDto.setContent("update content");
//        postService.update(postId,1L,postUpdateRequestDto);
//
//        postService.delete(postId,1L);
//    }
//
//    @Test
//    void 포스트_페이징(){
//        Member member = Member.builder()
//                .email("test")
//                .password("asegsrbasrbasrbas")
//                .role(Role.ROLE_MEMBER)
//                .provider(SocialLoginType.GOOGLE)
//                .build();
//
//        Long id = memberService.join(member);
//
//        for(int i=0;i<30;i++) {
//            PostSaveRequestDto postSaveRequestDto = new PostSaveRequestDto();
//            postSaveRequestDto.setTitle("test title"+String.valueOf(i));
//            postSaveRequestDto.setContent("test content"+String.valueOf(i));
//            postSaveRequestDto.setProblemId(1000L);
//            Long postId = postService.save(id,postSaveRequestDto);
//        }
//
//        List<PostPreviewDto> board = postService.getBoard(1);
//
//        for (PostPreviewDto postPreviewDto : board) {
//            System.out.println("postPreviewDto = " + postPreviewDto.getTitle());
//        }
//    }
//}
