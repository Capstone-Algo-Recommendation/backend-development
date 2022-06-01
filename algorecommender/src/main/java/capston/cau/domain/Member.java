package capston.cau.domain;

import capston.cau.domain.auth.SocialLoginType;
import capston.cau.domain.auth.Role;
import capston.cau.domain.board.Comment;
import capston.cau.domain.board.Post;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Column
    private String name;

    @Column(nullable = false,unique = true)
    private String email;

    @JsonIgnore
    @Column
    private String password;

    @NotNull
    @Enumerated(EnumType.STRING)
    private SocialLoginType provider;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column
    private String bojId;

    @Column
    private String refreshToken;

    @OneToMany(mappedBy="member",cascade = CascadeType.ALL)
    private List<MemberProblem> problemRelay = new ArrayList<>();

    @OneToMany(mappedBy="member",cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy="member",cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    public void setName(String name) {
        this.name = name;
    }

    public void setBojId(String bojId){
        this.bojId = bojId;
    }

    @Builder
    public Member(String email, String password, SocialLoginType provider, Role role){
        this.email = email;
        this.role = role;
        this.provider = provider;
        this.password = password;
    }

    public Member update(String name){
        this.name = name;
        return this;
    }

    public Role getRole() {
        return role;
    }

    public void setProvider(SocialLoginType valueOf) {
        this.provider = valueOf;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password){
        this.password=password;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken=refreshToken;
    }
}
