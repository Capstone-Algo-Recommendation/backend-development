package capston.cau.service;

import antlr.Token;
import capston.cau.domain.Member;
import capston.cau.domain.auth.Role;
import capston.cau.domain.auth.SocialLoginType;
import capston.cau.dto.member.request.MemberLoginRequestDto;
import capston.cau.dto.member.request.MemberRegisterRequestDto;
import capston.cau.dto.member.response.MemberLoginResponseDto;
import capston.cau.dto.member.response.MemberRegisterResponseDto;
import capston.cau.exception.*;
import capston.cau.jwt.JwtTokenProvider;
import capston.cau.jwt.dto.TokenRequestDto;
import capston.cau.jwt.dto.TokenResponseDto;
import capston.cau.oauth.AccessToken;
import capston.cau.oauth.profile.ProfileDto;
import capston.cau.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SignService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProviderService providerService;

    @Transactional
    public MemberRegisterResponseDto registerMember(MemberRegisterRequestDto requestDto) {
        validateDuplicated(requestDto.getEmail());
        Member member = memberRepository.save(
                Member.builder()
                        .email(requestDto.getEmail())
                        .password(passwordEncoder.encode(requestDto.getPassword()))
                        .role(Role.ROLE_GUEST)
                        .provider(SocialLoginType.LOCAL)
                        .build());
        return MemberRegisterResponseDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .build();
    }

    public void validateDuplicated(String email) {
        if (memberRepository.findByEmail(email).isPresent())
            throw new MemberEmailAlreadyExistsException();
    }

    public MemberLoginResponseDto loginMember(MemberLoginRequestDto requestDto) {
        Member member = memberRepository.findByEmail(requestDto.getEmail()).orElseThrow(LoginFailureException::new);
        if (!passwordEncoder.matches(requestDto.getPassword(), member.getPassword()))
            throw new LoginFailureException();
        member.updateRefreshToken(jwtTokenProvider.createRefreshToken());
        return new MemberLoginResponseDto(member.getId(),
                jwtTokenProvider.createToken(requestDto.getEmail()),
                member.getRefreshToken(),member.getRole().toString());
    }

    public boolean logoutMember(HttpServletRequest request){
        String token = jwtTokenProvider.resolveToken(request);
        jwtTokenProvider.makeTokenExpire(token);
        return true;
    }

    @Transactional
    public MemberLoginResponseDto loginMemberByProviderTest(String code, SocialLoginType provider) {
        AccessToken accessToken = providerService.getAccessToken(code, provider);
        ProfileDto profile = providerService.getProfile(accessToken.getAccess_token(), provider);

        Optional<Member> findMember = memberRepository.findByEmailAndProvider(profile.getEmail(), provider);
        if (findMember.isPresent()) {
            Member member = findMember.get();
            member.updateRefreshToken(jwtTokenProvider.createRefreshToken());
            return new MemberLoginResponseDto(member.getId(), jwtTokenProvider.createToken(findMember.get().getEmail()), member.getRefreshToken(),member.getRole().toString());
        } else {
            Member saveMember = saveMember(profile, provider);
            saveMember.updateRefreshToken(jwtTokenProvider.createRefreshToken());
            return new MemberLoginResponseDto(saveMember.getId(), jwtTokenProvider.createToken(saveMember.getEmail()), saveMember.getRefreshToken(),saveMember.getRole().toString());
        }
    }

    @Transactional
    public MemberLoginResponseDto loginMemberByProvider(AccessToken accessToken, SocialLoginType provider) {
        ProfileDto profile = providerService.getProfile(accessToken.getAccess_token(), provider);

        Optional<Member> findMember = memberRepository.findByEmailAndProvider(profile.getEmail(), provider);
        if (findMember.isPresent()) {
            Member member = findMember.get();
            member.updateRefreshToken(jwtTokenProvider.createRefreshToken());
            return new MemberLoginResponseDto(member.getId(), jwtTokenProvider.createToken(findMember.get().getEmail()), member.getRefreshToken(),member.getRole().toString());
        } else {
            Member saveMember = saveMember(profile, provider);
            saveMember.updateRefreshToken(jwtTokenProvider.createRefreshToken());
            return new MemberLoginResponseDto(saveMember.getId(), jwtTokenProvider.createToken(saveMember.getEmail()), saveMember.getRefreshToken(),saveMember.getRole().toString());
        }
    }

    private Member saveMember(ProfileDto profile, SocialLoginType provider) {
        Member member = Member.builder()
                .email(profile.getEmail())
                .password(UUID.randomUUID().toString())
                .provider(provider)
                .role(Role.ROLE_GUEST)
                .build();
        Member saveMember = memberRepository.save(member);
        return saveMember;
    }

    @Transactional
    public TokenResponseDto reIssue(TokenRequestDto requestDto) {
        if (!jwtTokenProvider.validateTokenExpiration(requestDto.getRefreshToken()))
            throw new InvalidRefreshTokenException();

        Member member = findMemberByToken(requestDto.getAccessToken());

        if (!member.getRefreshToken().equals(requestDto.getRefreshToken()))
            throw new InvalidRefreshTokenException();

        String accessToken = jwtTokenProvider.createToken(member.getEmail());
        String refreshToken = jwtTokenProvider.createRefreshToken();

        member.updateRefreshToken(refreshToken);
        return new TokenResponseDto(accessToken, refreshToken);
    }

    public Member findMemberByToken(String token) {
        Authentication auth = jwtTokenProvider.getAuthentication(token);
        if(jwtTokenProvider.validateTokenExpiration(token)==false){
            System.out.println("validate error");
            throw new AccessTokenInvalidatedException();
        }

//        System.out.println("validate error not happened");
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String username = userDetails.getUsername();
        return memberRepository.findByEmail(username).orElseThrow(MemberNotFoundException::new);
    }

}
