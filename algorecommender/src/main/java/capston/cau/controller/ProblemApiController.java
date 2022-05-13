package capston.cau.controller;

import capston.cau.service.ProblemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/problem")
public class ProblemApiController {

    private final ProblemService problemService;

    @GetMapping("/recommendation")
    public String getProblemRecommend(){
        return problemService.getRecommendProblems();
    }

}
