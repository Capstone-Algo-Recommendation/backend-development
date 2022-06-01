package capston.cau.jwt;

import capston.cau.exception.MemberNotFoundException;
import capston.cau.jwt.member.MemberDetails;
import capston.cau.jwt.member.MemberDetailsService;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

@Setter
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${spring.jwt.secretKey}")
    private String secretKey;

    private long tokenValidTime = 1000L*60*60*24*60;
    private long refreshTokenValidTime = 1000L*60*60*24*1;

    private final MemberDetailsService memberDetailsService;

    @PostConstruct
    protected void init(){
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(String email){
        Claims claims = Jwts.claims().setSubject(email);
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime()+tokenValidTime))
                .signWith(SignatureAlgorithm.HS256,secretKey)
                .compact();
    }

    public String createRefreshToken(){
        Date now = new Date();
        return Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime()+refreshTokenValidTime))
                .signWith(SignatureAlgorithm.HS256,secretKey)
                .compact();
    }

    public Authentication getAuthentication(String token){
        UserDetails userDetails = memberDetailsService.loadUserByUsername(getMemberEmail(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getMemberEmail(String token) {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
        } catch(ExpiredJwtException e) {
            return e.getClaims().getSubject();
        } catch(SignatureException e){
            throw new MemberNotFoundException();
        } catch (Exception e){
             throw new MemberNotFoundException();
        }
    }

    public String resolveToken(HttpServletRequest req) {
        return req.getHeader("X-AUTH-TOKEN");
    }

    public boolean validateTokenExpiration(String token) {
        try {
            Date now = new Date();
            Date expiration = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getExpiration();
            System.out.println("expiration = " + expiration);
            if(Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getExpiration().after(now)){
                return true;
            }else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public void makeTokenExpire(String token){
        Date now = new Date();
        Claims body = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        body.setExpiration(new Date(now.getTime()));
        Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().setExpiration(new Date(now.getTime()));
    }

}
