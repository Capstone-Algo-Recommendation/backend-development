package capston.cau.controller;

import capston.cau.dto.SingleResult;
import capston.cau.jwt.dto.TokenRequestDto;
import capston.cau.jwt.dto.TokenResponseDto;
import capston.cau.service.ResponseService;
import capston.cau.service.SignService;
//import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class HomeController {

    private final SignService signService;
    private final ResponseService responseService;

//    @ApiOperation(value="테스트 홈",notes = "인증테스트")
    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }

    @PostMapping("/reissue")
    public SingleResult<TokenResponseDto> reIssue(@RequestBody TokenRequestDto tokenRequestDto) {
        TokenResponseDto responseDto = signService.reIssue(tokenRequestDto);
        return responseService.getSingleResult(responseDto);
    }
}
