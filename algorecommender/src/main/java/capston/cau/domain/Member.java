package capston.cau.domain;

import capston.cau.domain.auth.AuthProvider;
import capston.cau.domain.auth.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false,unique = true)
    private String email;

    private String bojId;

    @Column(nullable = false)
    private Boolean emailVerified = false;

    @JsonIgnore
    @Column
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthProvider provider;

    @Column
    private String providerId;

    @Column
    private Role role;

    @OneToMany(mappedBy="member",cascade = CascadeType.ALL)
    private List<MemberProblem> problemRelay = new ArrayList<>();

    public void setName(String name) {
        this.name = name;
    }

    public void setBojId(String bojId){
        this.bojId = bojId;
    }

    @Builder
    public Member(String name, String email,Role role,Boolean emailVerified,
                  String password,AuthProvider provider, String providerId){
        this.name = name;
        this.email = email;
        this.role = role;
        this.emailVerified = emailVerified;
        this.password = password;
        this.provider = provider;
        this.providerId = providerId;
    }

    public Member update(String name){
        this.name = name;
        return this;
    }

    public void setProvider(AuthProvider valueOf) {
        this.provider = valueOf;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password){
        this.password=password;
    }
}
