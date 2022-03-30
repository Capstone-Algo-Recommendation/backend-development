package capston.cau.service;

import capston.cau.domain.Problem;
import capston.cau.repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProblemService {

    private final ProblemRepository problemRepository;

    @Transactional
    public Long addProblem(Problem problem){
        if(validateDuplicateProblem(problem)) {
            problemRepository.save(problem);
        }
        return problem.getId();
    }

    public Problem findById(Long id){
        return problemRepository.findById(id);
    }

    private boolean validateDuplicateProblem(Problem problem){
        Problem findProblem = problemRepository.findById(problem.getId());
        if(findProblem!=null) {
            return false;
//            throw new IllegalStateException("이미 존재하는 문제입니다.");
        }
        return true;
    }

}
