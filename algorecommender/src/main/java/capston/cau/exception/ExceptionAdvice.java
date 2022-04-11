package capston.cau.exception;

import capston.cau.dto.Result;
import capston.cau.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionAdvice {

    private final ResponseService responseService;

    @ExceptionHandler(MemberEmailAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result userEmailAlreadyExistsException() {
        return responseService.getFailureResult(-101, "이미 존재하는 이메일입니다.");
    }

    @ExceptionHandler(LoginFailureException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result loginFailureException() {
        return responseService.getFailureResult(-102, "아이디 혹은 비밀번호가 틀립니다.");
    }

    @ExceptionHandler(AuthenticationEntryPointException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result authenticationEntryPointException() {
        return responseService.getFailureResult(-102, "인증이 필요합니다.");
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result accessDeniedException() {
        return responseService.getFailureResult(-103, "권한이 필요합니다.");
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result invalidRefreshTokenException() {
        return responseService.getFailureResult(-104, "Refresh Token이 유효하지 않습니다.");
    }

    @ExceptionHandler(MemberNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result memberNotFoundException(){
        return responseService.getFailureResult(-105, "Invalid Access Token");
    }

    @ExceptionHandler(AccessTokenInvalidatedException.class)
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Result accessTokenInvalidatedException(){
        return responseService.getFailureResult(-106,"Access Token Invalidated! Login Again");
    }

    @ExceptionHandler(ProblemNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result problemNotFoundException(){
        return responseService.getFailureResult(-107,"Problem is not found");
    }
/*
    @ExceptionHandler(EmailNotAuthenticatedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result emailAuthenticationException() {
        return responseService.getFailureResult(-105, "이메일 인증이 필요합니다.");
    }

    @ExceptionHandler(EmailAuthTokenNotFountException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result emailAuthTokenNotFountException() {
        return responseService.getFailureResult(-106, "유효하지 않은 인증요청입니다.");
    }
*/
}