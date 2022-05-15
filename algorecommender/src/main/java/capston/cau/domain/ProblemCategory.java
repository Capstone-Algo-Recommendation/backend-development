package capston.cau.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class ProblemCategory {

    @Id
    @GeneratedValue
    @Column(name="problem_category_id")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="problem_id")
    private Problem problem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="category_id")
    private CategoryName category;

    public void setProblem(Problem problem) {
        this.problem = problem;
    }

    public void setCategory(CategoryName category) {
        this.category = category;
    }
}
