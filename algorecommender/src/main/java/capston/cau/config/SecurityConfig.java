package capston.cau.config;

import capston.cau.domain.auth.Role;
import capston.cau.jwt.JwtAuthenticationFilter;
import capston.cau.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;

//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations())
//                .antMatchers(
//                        "/v2/api-docs",
//                        "/swagger-resources",
//                        "/swagger-resources/**",
//                        "/configuration/ui",
//                        "/configuration/security",
//                        "/swagger-ui.html",
//                        "/webjars/**",
//                        "/v3/api-docs/**",
//                        "/swagger-ui/**");
//    }
    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
        .httpBasic().disable() // rest api이므로 기본설정 미사용
        .csrf().disable() // rest api이므로 csrf 보안 미사용
        .formLogin().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // jwt로 인증하므로 세션 미사용
        .and()
        .authorizeRequests()
        .antMatchers("/sign/**","/social/**","/exception/**").permitAll()
        .antMatchers("/api/member/me/init**").hasAnyRole("GUEST","MEMBER")
//                .antMatchers("/api/board/**")
        .anyRequest().hasAnyRole("MEMBER")
        .and()
        .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint())
        .and()
        .exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler())
        .and()
        .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class); // jwt 필터 추가
    }
}
