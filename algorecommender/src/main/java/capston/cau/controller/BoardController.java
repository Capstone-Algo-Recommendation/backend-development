package capston.cau.controller;

import capston.cau.domain.Member;
import capston.cau.dto.Result;
import capston.cau.dto.SingleResult;
import capston.cau.dto.board.CommentRequestDto;
import capston.cau.dto.board.PostResponseDto;
import capston.cau.dto.board.PostSaveRequestDto;
import capston.cau.dto.board.PostUpdateRequestDto;
import capston.cau.jwt.JwtTokenProvider;
import capston.cau.service.PostService;
import capston.cau.service.ResponseService;
import capston.cau.service.SignService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final PostService postService;
    private final SignService signService;
    private final ResponseService responseService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping()
    public String boardMain(){
        //메인 페이지, 페이징 구현해야
        return "";
    }

    @PostMapping()
    public SingleResult<Long> postBoard(HttpServletRequest request, @RequestBody PostSaveRequestDto postSaveRequestDto){
        String token = jwtTokenProvider.resolveToken(request);
        Member memberByToken = signService.findMemberByToken(token);
        Long postId = postService.save(memberByToken.getId(),postSaveRequestDto);
        return responseService.getSingleResult(postId);
    }

    @GetMapping("/{postId}")
    public SingleResult<PostResponseDto> boardDetail(@PathVariable Long postId){
        return responseService.getSingleResult(postService.findById(postId));
    }

    //문제?
    @PatchMapping("/{postId}")
    public SingleResult<PostResponseDto> boardUpdate(HttpServletRequest request,@PathVariable Long postId,@RequestBody PostUpdateRequestDto postUpdateRequestDto){
        String token = jwtTokenProvider.resolveToken(request);
        Member memberByToken = signService.findMemberByToken(token);
        postService.update(postId, memberByToken.getId(), postUpdateRequestDto);
        return responseService.getSingleResult(postService.findById(postId));
    }

    @GetMapping("/delete/{postId}")
    public Result boardDelete(HttpServletRequest request, @PathVariable Long postId){
        String token = jwtTokenProvider.resolveToken(request);
        Member memberByToken = signService.findMemberByToken(token);
        postService.delete(postId, memberByToken.getId());
        return responseService.getSuccessResult();
    }

    @PostMapping("/{postId}/comment")
    public Result boardAddComment(HttpServletRequest request, @PathVariable Long postId, @RequestBody CommentRequestDto commentRequestDto){
        String token = jwtTokenProvider.resolveToken(request);
        Member memberByToken = signService.findMemberByToken(token);
        postService.addComment(memberByToken.getId(),postId,commentRequestDto);
        return responseService.getSuccessResult();
    }

    @PatchMapping("/{postId}/comment")
    public Result boardUpdateComment(HttpServletRequest request, @PathVariable Long postId, @RequestBody CommentRequestDto commentRequestDto){
        String token = jwtTokenProvider.resolveToken(request);
        Member memberByToken = signService.findMemberByToken(token);
        postService.updateComment(memberByToken.getId(),postId,commentRequestDto);
        return responseService.getSuccessResult();
    }

    @GetMapping("/comment/delete/{commentId}")
    public Result boardDeleteComment(HttpServletRequest request,@PathVariable Long commentId){
        String token = jwtTokenProvider.resolveToken(request);
        Member memberByToken = signService.findMemberByToken(token);
        postService.deleteComment(memberByToken.getId(),commentId);
        return responseService.getSuccessResult();
    }

}
