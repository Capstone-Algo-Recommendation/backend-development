package capston.cau.service;

import capston.cau.domain.Problem;
import capston.cau.dto.problem.ProblemDto;
import capston.cau.repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        return problemRepository.findById(id).get();
    }

    private boolean validateDuplicateProblem(Problem problem){
        Problem findProblem = problemRepository.findById(problem.getId()).orElse(null);
        if(findProblem!=null) {
            return false;
//            throw new IllegalStateException("이미 존재하는 문제입니다.");
        }
        return true;
    }

    public String getRecommendProblems(){

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();;
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<String> entity = new HttpEntity<String>("{\"id\":\"1\", \"name\":\"CTB\"}", headers);
        String returnData = "";
        try {
            ResponseEntity<String> resp = restTemplate.postForEntity("http://localhost:5050/tospring", entity, String.class);
            returnData = resp.getBody();
        }catch (HttpStatusCodeException e){
            System.out.println("e = " + e);
        }
        return returnData;
//        Map<String,String> params = new HashMap<>();
//        params.put("test","testFlask");
//        RestTemplate rt = new RestTemplate();
//        String response = "";
//        try{
//            response = rt.getForObject("http://localhost:5050/tospring",String.class,params);
//        }catch (HttpStatusCodeException e){
//            System.out.println("e = " + e);
//        }
//        System.out.println("response = " + response);
//        return response;
    }

}
