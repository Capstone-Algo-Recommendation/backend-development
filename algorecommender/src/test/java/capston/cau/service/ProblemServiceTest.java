package capston.cau.service;

import capston.cau.domain.Problem;
import capston.cau.repository.ProblemRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ProblemServiceTest {

    @Autowired
    private ProblemService problemService;

    @Test
    void 문제_추가(){
        Problem problem1 = new Problem(1000L,"A+B","www.test1000.com");
        Problem problem2 = new Problem(1001L,"A-B","www.test1001.com");

        problemService.addProblem(problem1);
        problemService.addProblem(problem2);

        Problem findProblem1 = problemService.findById(1000L);
        assertThat(findProblem1.getName()).isEqualTo("A+B");
    }

}