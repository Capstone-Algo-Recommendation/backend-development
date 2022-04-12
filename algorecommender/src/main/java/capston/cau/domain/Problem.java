package capston.cau.domain;

import capston.cau.domain.board.Post;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Problem {

    @Id @NotNull
    @Column(name="problem_id")
    private Long id;

    private String name;
    private String url;

    @OneToMany(mappedBy = "problem",cascade = CascadeType.ALL)
    private List<MemberProblem> memberRelay = new ArrayList<>();

    @OneToMany(mappedBy ="problem",cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<>();

    protected Problem(){

    }

    public Problem(Long id, String name, String url){
        this.id = id;
        this.name = name;
        this.url = url;
    }

}
