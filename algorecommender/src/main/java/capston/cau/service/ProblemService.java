package capston.cau.service;

import capston.cau.domain.CategoryName;
import capston.cau.domain.Problem;
import capston.cau.domain.ProblemCategory;
import capston.cau.dto.problem.FlaskResponse;
import capston.cau.dto.problem.ProblemDto;
import capston.cau.repository.MemberProblemRepository;
import capston.cau.repository.ProblemRepository;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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
    private final MemberService memberService;
    private final MemberProblemRepository relayRepository;

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

    public List<String> findCategoryNames(){
        List<CategoryName> categories = problemRepository.findCategories().orElse(null);
        List<String> categoryNames = new ArrayList<>();
        if(categories ==null)
            return categoryNames;
        for (CategoryName category : categories) {
            categoryNames.add(category.getCategory());
        }
        return categoryNames;
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
//            throw new IllegalStateException("?????? ???????????? ???????????????.");
        }
        return true;
    }

    public ProblemDto findByIdToDto(Long memberId, Long id){
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
        Long relayFlag = relayRepository.isRelayed(memberId,id);
        if (relayFlag != -1L){
            problemDto.setMemo(relayRepository.findById(relayFlag).getMemo());
            problemDto.setStatus(relayRepository.findById(relayFlag).getProblemStatus());
        }

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

    //15??? ????????? ???????????? ???????????? ?????? ??????
    public List<ProblemDto> getRecommendProblems(Long memberId){

        RestTemplate restTemplate = new RestTemplate();
        List<Problem> memberSolvedProblems = memberService.getMemberSolvedProblems(memberId);

        if(memberSolvedProblems.size()<=15)
            return coldStartProblems(memberId);

        HttpHeaders headers = new HttpHeaders();;
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        
        JsonObject jsonObject = new JsonObject();

        JsonArray problemArr = new JsonArray();
        for (Problem prob : memberSolvedProblems) {
            JsonObject problemInfo = new JsonObject();
            problemInfo.addProperty("id",prob.getId());
            problemArr.add(problemInfo);
        }

        jsonObject.add("data", problemArr);
        jsonObject.addProperty("email",memberId);

        HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(), headers);
        String returnData = "";
        try {
            List<ProblemDto> recommList = new ArrayList<>();
            ResponseEntity<String> resp = restTemplate.postForEntity("http://34.64.49.195:5050/tospring", entity, String.class);
            returnData = resp.getBody();

            FlaskResponse ret = new Gson().fromJson(returnData,FlaskResponse.class);
            for (Long datum : ret.getData()) {
                ProblemDto probData = this.findByIdToDto(memberId,datum);
                if(probData != null)
                    recommList.add(probData);
            }
            return recommList;
        }catch (HttpStatusCodeException e){
            System.out.println("e = " + e);
        }
        return null;
    }

    private List<ProblemDto> coldStartProblems(Long memberId){
        List<ProblemDto> recommList = new ArrayList<>();
        Long[] starterPack = {2557L,10718L,10171L,25083L,1000L,
                                1330L,9498L,2753L,14681L,2884L,
                                2739L,10950L,8393L,15552L,2741L};

        for (Long aLong : starterPack) {
            ProblemDto probData = this.findByIdToDto(memberId,aLong);
            recommList.add(probData);
        }
        return recommList;
    }

}
