package capston.cau.controller;

import capston.cau.domain.auth.SocialLoginType;
import capston.cau.dto.SingleResult;
import capston.cau.dto.member.request.MemberLoginRequestDto;
import capston.cau.dto.member.request.MemberRegisterRequestDto;
import capston.cau.dto.member.response.MemberLoginResponseDto;
import capston.cau.dto.member.response.MemberRegisterResponseDto;
import capston.cau.oauth.AuthCode;
import capston.cau.service.ResponseService;
import capston.cau.service.SignService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sign")
public class SignController {

    private final SignService signService;
    private final ResponseService responseService;

    @PostMapping("/register")
    public SingleResult<MemberRegisterResponseDto> register(@RequestBody MemberRegisterRequestDto requestDto) {
        System.out.println("requestDto = " + requestDto);
        MemberRegisterResponseDto responseDto = signService.registerMember(requestDto);
        return responseService.getSingleResult(responseDto);
    }

    @PostMapping("/login")
    public SingleResult<MemberLoginResponseDto> login(@RequestBody MemberLoginRequestDto requestDto) {
        MemberLoginResponseDto responseDto = signService.loginMember(requestDto);
        return responseService.getSingleResult(responseDto);
    }

    @PostMapping("/login/{provider}")
    public SingleResult<MemberLoginResponseDto> loginBySocial(@RequestBody AuthCode authCode, @PathVariable String provider) {
        MemberLoginResponseDto responseDto = signService.loginMemberByProvider(authCode.getCode(), getProviderValue(provider));
        return responseService.getSingleResult(responseDto);
    }

    //TODO error Handling
    private SocialLoginType getProviderValue(String provider)throws IllegalArgumentException{
        return SocialLoginType.valueOf(provider.toUpperCase());
    }

}
