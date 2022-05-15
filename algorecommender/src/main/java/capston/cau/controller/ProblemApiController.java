package capston.cau.controller;

import capston.cau.domain.Problem;
import capston.cau.dto.MultipleResult;
import capston.cau.dto.SingleResult;
import capston.cau.dto.problem.ProblemDto;
import capston.cau.service.ProblemService;
import capston.cau.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/problem")
public class ProblemApiController {

    private final ProblemService problemService;
    private final ResponseService responseService;

    @GetMapping("/recommendation")
    public String getProblemRecommend(){
        return problemService.getRecommendProblems();
    }

    @GetMapping("/{problemId}")
    public SingleResult<ProblemDto> getProblemDetail(@PathVariable Long problemId){
        return responseService.getSingleResult(problemService.findByIdToDto(problemId));
    }

    @GetMapping("/filter")
    public MultipleResult<ProblemDto> test(@RequestParam("level") Long level, @RequestParam(name = "category",required = false)String category, @RequestParam("page")Integer page){
        if(category==null)
            return responseService.getMultipleResult(problemService.findProblemByLevel(level,page));
        return responseService.getMultipleResult(problemService.findProblemByLevelAndCategory(level,category,page));
    }

}
