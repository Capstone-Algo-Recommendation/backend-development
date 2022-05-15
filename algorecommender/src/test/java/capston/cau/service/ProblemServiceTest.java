package capston.cau.service;

import capston.cau.domain.CategoryName;
import capston.cau.domain.Member;
import capston.cau.domain.Problem;
import capston.cau.domain.ProblemCategory;
import capston.cau.domain.auth.SocialLoginType;
import capston.cau.dto.member.MemberDto;
import capston.cau.dto.problem.ProblemDto;
import capston.cau.repository.ProblemRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ProblemServiceTest {

    @Autowired
    private ProblemService problemService;

    @Autowired
    private ProblemRepository problemRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    EntityManager em;

    @Test
    void initTest() {

    }

    @Test
    void 문제_레벨_카테고리_조회_서비스_테스트(){
//        Long dp = problemService.findCategoryByName("dp");
        List<ProblemDto> problemDtos = problemService.findProblemByLevelAndCategory(19L,"dp",0);
        System.out.println("problemDtos.size() = " + problemDtos.size());
        for (ProblemDto problemDto : problemDtos) {
            System.out.println("problemDto.getName() = " + problemDto.getName());
            System.out.println("problemDto.getId() = " + problemDto.getId());
            System.out.println("problemDto.getLevel() = " + problemDto.getLevel());
        }
    }

    @Test
    void 문제_레벨_조회_서비스_테스트(){
        //none case test
        List<ProblemDto> problemByLevel = problemService.findProblemByLevel(3L, 0);
        System.out.println("problemByLevel.size() = " + problemByLevel.size());
//        for (ProblemDto problemDto : problemByLevel) {
//            System.out.println("problemDto.getName() = " + problemDto.getName());
//            System.out.println("problemDto.getCategories().toString() = " + problemDto.getCategories().toString());
//        }
    }

    @Test
    void 문제_조회_페이징_테스트(){
        //페이징
        List<Problem> problemByLevel = problemRepository.findProblemByLevel(15L, PageRequest.of(0, 5));

        CategoryName bruteforcing = problemRepository.findCategoryByName("bruteforcing").get();
        List<Problem> findProblem = problemRepository.findProblemByLevelAndCategory(11L, bruteforcing.getId(), PageRequest.of(0, 5));
        for (Problem problem : findProblem) {
            System.out.println("problem = " + problem.getName() + " " + problem.getId()+ " "+problem.getLevel());
        }

    }

    @Test
    void 문제_추가() {
        Problem problem1 = new Problem(1020L, "A+B", "www.test1000.com",2L);
        Problem problem2 = new Problem(1021L, "A-B", "www.test1001.com",2L);

        problemService.addProblem(problem1);
//        problemService.addProblem(problem1);
//        problemService.addProblem(problem2);

        CategoryName category = new CategoryName();
        category.setCategory("sort");

        problemRepository.saveCategory(category);
        problemRepository.setProblemCategory(problem1.getId(), category.getId());
        em.flush();
        em.clear();

        Problem findProblem = problemRepository.findByIdWithCategory(problem1.getId()).get();

        if (findProblem == null)
            System.out.println("null");
        else {
            for (ProblemCategory findProblemCategory : findProblem.getCategories()) {
                System.out.println("findProblemCategory.getCategory().getCategory() = " + findProblemCategory.getCategory().getCategory());
            }
        }
        problemRepository.removeProblem(findProblem);
        Problem findProblem2 = problemRepository.findByIdWithCategory(problem2.getId()).orElse(null);
        if (findProblem2 == null)
            System.out.println("null2");
        else {
            for (ProblemCategory findProblemCategory : findProblem.getCategories()) {
                System.out.println("findProblemCategory.getCategory().getCategory() = " + findProblemCategory.getCategory().getCategory());
            }
        }
//        Problem findProblem1 = problemService.findById(1000L);
//        assertThat(findProblem1.getName()).isEqualTo("A+B");
    }


    @Test
    void 문제_추가2(){
//        Problem problem1 = new Problem(1020L,"Test1","www.test1000.com");
        CategoryName sortCategory = new CategoryName();
        sortCategory.setCategory("sort");
        problemRepository.saveCategory(sortCategory);
        CategoryName cn = problemRepository.findCategoryByName("sort").orElse(null);
//        System.out.println("cn = " + cn.getCategory());
    }

    @Test
    void 멤버_문제_카테고리_가져오기(){

        Problem problem1 = new Problem(1020L,"Test1","www.test1000.com",0L);
        Problem problem2 = new Problem(1021L,"Test2","www.test1001.com",0L);

        CategoryName sortCategory = new CategoryName();
        sortCategory.setCategory("sort");

        CategoryName mathCategory = new CategoryName();
        mathCategory.setCategory("math");

        CategoryName testCategory = new CategoryName();
        testCategory.setCategory("test");

        problemService.addProblem(problem1);
        problemService.addProblem(problem2);

        problemService.addCategory(sortCategory);
        problemService.addCategory(mathCategory);
        problemService.addCategory(testCategory);

        problemService.setProblemCategory(problem1.getId(),sortCategory.getId());
        problemService.setProblemCategory(problem2.getId(),mathCategory.getId());
        problemService.setProblemCategory(problem2.getId(),testCategory.getId());
        em.flush();
        em.clear();

        Member member = new Member();
        member.setName("testmem");
        member.setEmail("asdf@asdf.com");
        member.setProvider(SocialLoginType.LOCAL);
        memberService.join(member);

        memberService.addTryingProblem(member.getId(),problem1.getId());
        memberService.addTryingProblem(member.getId(),problem2.getId());

        em.flush();
        em.clear();

        MemberDto memberProblemList = memberService.getMemberProblemList(member.getId(), "");

        for (ProblemDto problemDto : memberProblemList.getProblemList()) {
            System.out.println("problemDto.getName() = " + problemDto.getName());
            for (String category : problemDto.getCategories()) {
                System.out.println("category = " + category);
            }
        }
    }
}