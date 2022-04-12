package capston.cau.repository;

import capston.cau.domain.*;
import capston.cau.domain.auth.SocialLoginType;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

import static capston.cau.domain.QMember.*;
import static capston.cau.domain.QMemberProblem.memberProblem;
import static capston.cau.domain.QProblem.problem;


public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    Optional<Member> findByEmailAndProvider(String email, SocialLoginType provider);

}
