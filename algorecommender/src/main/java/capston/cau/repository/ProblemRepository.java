package capston.cau.repository;

import capston.cau.domain.Problem;
import capston.cau.domain.QProblem;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProblemRepository {

    private final EntityManager em;
    private JPAQueryFactory queryFactory;

    //test용 problem 객체 추가
    public void save(Problem problem){
        em.persist(problem);
    }

    public Optional<Problem> findById(Long id){
        queryFactory = new JPAQueryFactory(em);
        Problem findProblem = queryFactory.selectFrom(QProblem.problem)
                .where(QProblem.problem.id.eq(id))
                .fetchFirst();
        return Optional.ofNullable(findProblem);
    }




}
