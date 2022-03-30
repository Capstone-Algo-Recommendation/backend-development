package capston.cau.controller;

import capston.cau.service.ProblemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ProblemApiController {

    private final ProblemService problemService;

}
