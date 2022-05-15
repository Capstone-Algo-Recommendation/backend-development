package capston.cau.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class CategoryName {

    @Id
    @GeneratedValue
    @Column(name="category_id")
    private Long id;

    @Column
    @NotNull
    private String category;

    @OneToMany(mappedBy = "category",cascade = CascadeType.ALL)
    private List<ProblemCategory> problemRelay = new ArrayList<>();

    public void setCategory(String category) {
        this.category = category;
    }
}
