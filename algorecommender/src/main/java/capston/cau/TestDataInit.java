package capston.cau;

import capston.cau.domain.CategoryName;
import capston.cau.domain.Member;
import capston.cau.domain.Problem;
import capston.cau.domain.auth.Role;
import capston.cau.domain.auth.SocialLoginType;
import capston.cau.dto.board.PostSaveRequestDto;
import capston.cau.repository.MemberRepository;
import capston.cau.repository.ProblemRepository;
import capston.cau.service.MemberService;
import capston.cau.service.PostService;
import capston.cau.service.ProblemService;
import jdk.jfr.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TestDataInit {

    private final MemberService memberService;
    private final ProblemService problemService;
    private final PostService postService;

    @PostConstruct
    public void init(){
//        initCsvData();
    }

    public void initCsvData(){
        List<String> csvList = new ArrayList<String>();
        ClassPathResource rsc = new ClassPathResource("data/problemMeta.csv");
        BufferedReader br = null;

        try {
            File csv = rsc.getFile();
            String line = "";
            br = new BufferedReader(new FileReader(csv));
            br.readLine();
            while((line=br.readLine())!=null) { // readLine()은 파일에서 개행된 한 줄의 데이터를 읽어온다.
                parseStrToProb(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close(); // 사용 후 BufferedReader를 닫아준다.
                }
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void parseStrToProb(String ln){
        int len = ln.length();
        List<String> strArr = new ArrayList<>();
        String temp = "";
        boolean flag = false;
        for(int i=0;i<len;i++){
              if(ln.charAt(i)=='"'){
                  if(flag){
                      strArr.add(temp);
                      temp="";
                      flag = false;
                  }else{
                      flag = true;
                  }
              }else if(ln.charAt(i)==','){
                  if(flag){
                      temp+=ln.charAt(i);
                  }else{
                      if(temp!="")
                        strArr.add(temp);
                      temp = "";
                  }
              }else{
                  temp += ln.charAt(i);
              }
        }
        strArr.add(temp);

        Long problemId = Long.valueOf(strArr.get(1));
        String problemName = strArr.get(2);
        String problemUrl = "https://www.acmicpc.net/problem/"+strArr.get(1);

        List<String> categories = new ArrayList<>();
        String secondPart = strArr.get(3).substring(1, strArr.get(3).length()-1);
        int secondLen = secondPart.length();
        String categoryTemp = "";

        for(int i=0;i<secondLen;i++){
            if(secondPart.charAt(i)=='\''){
                if(categoryTemp.length()!=0){
                    categories.add(categoryTemp);
                    categoryTemp = "";
                }
                continue;
            }
            if(secondPart.charAt(i)==' '||secondPart.charAt(i)==',')
                continue;
            categoryTemp+=secondPart.charAt(i);
        }

        String levelStr = strArr.get(strArr.size()-1);
        Long level = Long.valueOf(levelStr);

        createProblemAndCategory(problemId,problemName,problemUrl,level,categories);
    }

    private void createProblemAndCategory(Long problemId,String problemName,String problemUrl,Long problemLevel,
                                          List<String> categoryName){
        Problem problem = new Problem(problemId,problemName,problemUrl,problemLevel);
        problemService.addProblem(problem);

        List<Long> categoryId = new ArrayList<>();
        for (String s : categoryName) {
            Long flag = problemService.findCategoryByName(s);
            if(flag ==-1L){
                CategoryName category = new CategoryName();
                category.setCategory(s);
                flag = problemService.addCategory(category);
            }
            categoryId.add(flag);
        }

        for (Long id : categoryId) {
            problemService.setProblemCategory(problem.getId(),id);
        }
    }

}
