package capston.cau.service;

import capston.cau.domain.CategoryName;
import capston.cau.domain.Problem;
import capston.cau.domain.ProblemCategory;
import capston.cau.dto.problem.ProblemDto;
import capston.cau.repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

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

    @Transactional
    public Long addCategory(CategoryName categoryName){
        problemRepository.saveCategory(categoryName);
        return categoryName.getId();
    }

    @Transactional
    public Long setProblemCategory(Long problemId, Long categoryId){
        Long id = problemRepository.setProblemCategory(problemId,categoryId);
        return id;
    }

    public Long findCategoryByName(String name){
        CategoryName categoryName = problemRepository.findCategoryByName(name).orElse(null);
        if(categoryName==null)
            return -1L;
        return categoryName.getId();
    }

    public Problem findById(Long id){
        Problem findProblem = problemRepository.findByIdWithCategory(id).orElse(null);
        if(findProblem==null)
            findProblem = problemRepository.findById(id).get();
        return findProblem;
    }

    private boolean validateDuplicateProblem(Problem problem){
        Problem findProblem = problemRepository.findById(problem.getId()).orElse(null);
        if(findProblem!=null) {
            return false;
//            throw new IllegalStateException("이미 존재하는 문제입니다.");
        }
        return true;
    }

    public ProblemDto findByIdToDto(Long id){
        Problem findProblem = problemRepository.findByIdWithCategory(id).orElse(null);
        if(findProblem==null)
            findProblem = problemRepository.findById(id).orElse(null);
        if(findProblem==null)
            return null;
        List<String> categories = new ArrayList<>();
        List<ProblemCategory> pc = findProblem.getCategories();

        for (ProblemCategory problemCategory : pc) {
            categories.add(problemCategory.getCategory().getCategory());
        }

        ProblemDto problemDto = ProblemDto.builder()
                .id(findProblem.getId())
                .name(findProblem.getName())
                .url(findProblem.getUrl())
                .level(findProblem.getLevel())
                .categories(categories)
                .build();

        return problemDto;
    }

    public List<ProblemDto> findProblemByLevel(Long level,Integer page){
        List<Problem> problemByLevel = problemRepository.findProblemByLevel(level, PageRequest.of(page, 10));
        List<ProblemDto> problemDtos = new ArrayList<>();
        for (Problem problem : problemByLevel) {
            List<String> categories = problemRepository.findProblemCategory(problem.getId());
            ProblemDto dto = ProblemDto.builder()
                    .id(problem.getId())
                    .name(problem.getName())
                    .url(problem.getUrl())
                    .level(problem.getLevel())
                    .categories(categories)
                    .build();
            problemDtos.add(dto);
        }
        return problemDtos;
    }

    public List<ProblemDto> findProblemByLevelAndCategory(Long level,String category,Integer page){
        Long categoryId = findCategoryByName(category);
        List<ProblemDto> problemDtos = new ArrayList<>();
        if(categoryId == -1L)
            return problemDtos;
        List<Problem> findProblems = problemRepository.findProblemByLevelAndCategory(level, categoryId, PageRequest.of(page, 10));


        for (Problem findProblem : findProblems) {
            List<String> categories = problemRepository.findProblemCategory(findProblem.getId());
            ProblemDto dto = ProblemDto.builder()
                    .id(findProblem.getId())
                    .name(findProblem.getName())
                    .url(findProblem.getUrl())
                    .level(findProblem.getLevel())
                    .categories(categories)
                    .build();
            problemDtos.add(dto);
        }
        return problemDtos;
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
    }



}
