package capston.cau.domain;

import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;
    private String bojId;

    @OneToMany(mappedBy="member",cascade = CascadeType.ALL)
    private List<MemberProblem> problemRelay = new ArrayList<>();

    public void setName(String name) {
        this.name = name;
    }

    public void setBojId(String bojId){
        this.bojId = bojId;
    }
}
