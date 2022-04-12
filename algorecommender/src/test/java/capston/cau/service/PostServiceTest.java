package capston.cau.service;

import capston.cau.domain.Member;
import capston.cau.domain.auth.Role;
import capston.cau.domain.auth.SocialLoginType;
import capston.cau.dto.board.PostResponseDto;
import capston.cau.dto.board.PostSaveRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.security.Provider;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private EntityManager em;

    @Test
    @Rollback(false)
    void 글_작성(){

        Member member = Member.builder()
                .email("test")
                .password("asegsrbasrbasrbas")
                .role(Role.ROLE_MEMBER)
                .provider(SocialLoginType.GOOGLE)
                .build();

        Long id = memberService.join(member);

        PostSaveRequestDto postSaveRequestDto = new PostSaveRequestDto();
        postSaveRequestDto.setTitle("test title");
        postSaveRequestDto.setContent("test content");
        postSaveRequestDto.setProblemId(1000L);

        Long postId = postService.save(id,postSaveRequestDto);
        em.flush();
        em.clear();
        PostResponseDto byId = postService.findById(postId);

        postService.delete(postId,1L);

    }

}