package capston.cau.jwt.member;

import capston.cau.domain.Member;
import capston.cau.exception.MemberNotFoundException;
import capston.cau.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email).orElse(null);
        if(member==null){
            throw new MemberNotFoundException();
        }
        List<GrantedAuthority> test = new ArrayList<>();
        test.add(new SimpleGrantedAuthority(member.getRole().toString()));

        return MemberDetails.builder()
                .email(member.getEmail())
                .password(member.getPassword())
                .authorities(test)
                .build();
    }
}
