package capston.cau.repository;

import capston.cau.domain.*;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jdk.jfr.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
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
    public void saveCategory(CategoryName category){em.persist(category);}

    public Long setProblemCategory(Long problemId, Long categoryId){
        ProblemCategory problemCategory = new ProblemCategory();
        Problem findProblem = findById(problemId).orElse(null);
        CategoryName findCategory = findCategoryById(categoryId).orElse(null);
        problemCategory.setProblem(findProblem);
        problemCategory.setCategory(findCategory);
        em.persist(problemCategory);

        return problemCategory.getId();
    }

    public Optional<Problem> findById(Long id){
        queryFactory = new JPAQueryFactory(em);
        Problem findProblem = queryFactory.selectFrom(QProblem.problem)
                .where(QProblem.problem.id.eq(id))
                .fetchOne();
        return Optional.ofNullable(findProblem);
    }

    public List<String> findProblemCategory(Long problemId){
        queryFactory = new JPAQueryFactory(em);
        Problem findProblem = queryFactory.selectFrom(QProblem.problem)
                .where(QProblem.problem.id.eq(problemId))
                .leftJoin(QProblem.problem.categories, QProblemCategory.problemCategory)
                .fetchJoin()
                .leftJoin(QProblemCategory.problemCategory.category, QCategoryName.categoryName)
                .fetchJoin()
                .fetchOne();

        if(findProblem==null)
            return null;

        List<String> categories = new ArrayList<>();
        for (ProblemCategory category : findProblem.getCategories()) {
            categories.add(category.getCategory().getCategory());
        }
        return categories;
    }

    public List<Problem> findProblemByLevel(Long level, Pageable pageable){
        queryFactory = new JPAQueryFactory(em);
        List<Problem> fetch = queryFactory.selectFrom(QProblem.problem)
                .where(QProblem.problem.level.eq(level))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(QProblem.problem.id.asc())
                .fetch();
        return fetch;
    }

    public List<Problem> findProblemByLevelAndCategory(Long level,Long categoryId,Pageable pageable){
        queryFactory = new JPAQueryFactory(em);
        List<Problem> fetch = queryFactory.selectFrom(QProblem.problem)
                .where(QProblem.problem.level.eq(level))
                .join(QProblem.problem.categories, QProblemCategory.problemCategory)
                .fetchJoin()
                .join(QProblemCategory.problemCategory.category, QCategoryName.categoryName)
                .where(QCategoryName.categoryName.id.eq(categoryId))
                .fetchJoin()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return fetch;
    }

    public Optional<Problem> findByIdWithCategory(Long id){
        queryFactory = new JPAQueryFactory(em);
        Problem fetch = queryFactory.selectFrom(QProblem.problem)
                .join(QProblem.problem.categories, QProblemCategory.problemCategory)
                .fetchJoin()
                .join(QProblemCategory.problemCategory.category, QCategoryName.categoryName)
                .where(QProblemCategory.problemCategory.problem.id.eq(id))
                .fetchJoin()
                .fetchFirst();

        return Optional.ofNullable(fetch);
    }

    public Optional<CategoryName> findCategoryById(Long id){
        queryFactory = new JPAQueryFactory(em);
        CategoryName findCategory = queryFactory.selectFrom(QCategoryName.categoryName)
                .where(QCategoryName.categoryName.id.eq(id))
                .fetchFirst();
        return Optional.ofNullable(findCategory);
    }

    public Optional<CategoryName> findCategoryByName(String name){
        queryFactory = new JPAQueryFactory(em);
        CategoryName findCategory = queryFactory.selectFrom(QCategoryName.categoryName)
                .where(QCategoryName.categoryName.category.eq(name))
                .fetchOne();
        return Optional.ofNullable(findCategory);
    }

    public void removeProblem(Problem problem) {
        em.remove(problem);
    }
}
