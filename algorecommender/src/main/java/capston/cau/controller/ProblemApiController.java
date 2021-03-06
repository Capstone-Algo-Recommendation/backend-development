package capston.cau.controller;

import capston.cau.domain.CategoryName;
import capston.cau.domain.Member;
import capston.cau.domain.Problem;
import capston.cau.dto.MultipleResult;
import capston.cau.dto.SingleResult;
import capston.cau.dto.problem.ProblemDto;
import capston.cau.dto.problem.ProblemMemoRequest;
import capston.cau.jwt.JwtTokenProvider;
import capston.cau.service.MemberService;
import capston.cau.service.ProblemService;
import capston.cau.service.ResponseService;
import capston.cau.service.SignService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/problem")
public class ProblemApiController {

    private final ProblemService problemService;
    private final ResponseService responseService;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberService memberService;
    private final SignService signService;

    @GetMapping("/recommendation")
    public MultipleResult<ProblemDto> getProblemRecommend(HttpServletRequest request){
        String token = jwtTokenProvider.resolveToken(request);
        Member memberByToken = signService.findMemberByToken(token);
        List<ProblemDto> recommendProblems = problemService.getRecommendProblems(memberByToken.getId());
        return responseService.getMultipleResult(recommendProblems);
    }

    @GetMapping("/{problemId}")
    public SingleResult<ProblemDto> getProblemDetail(HttpServletRequest request,@PathVariable Long problemId){
        String token = jwtTokenProvider.resolveToken(request);
        Member memberByToken = signService.findMemberByToken(token);
        return responseService.getSingleResult(problemService.findByIdToDto(memberByToken.getId(),problemId));
    }

    @GetMapping("/filter")
    public MultipleResult<ProblemDto> problemFilter(@RequestParam("level") Long level, @RequestParam(name = "category",required = false)String category, @RequestParam("page")Integer page){
        if(category==null)
            return responseService.getMultipleResult(problemService.findProblemByLevel(level,page));
        return responseService.getMultipleResult(problemService.findProblemByLevelAndCategory(level,category,page));
    }

    @PostMapping("/{problemId}/memo")
    public SingleResult<Long> postMemoToProblem(HttpServletRequest request, @PathVariable Long problemId, @RequestBody ProblemMemoRequest memoRequest){
        String token = jwtTokenProvider.resolveToken(request);
        Member memberByToken = signService.findMemberByToken(token);
        Long result = memberService.addMemoToProblem(memberByToken.getId(), problemId,memoRequest.getMemo());
        return responseService.getSingleResult(result);
    }

    @GetMapping("/list/category")
    public SingleResult<List<String>> getCategoryNames(){
        return responseService.getSingleResult(problemService.findCategoryNames());
    }
}
