package capston.cau.domain.board;

import capston.cau.domain.BaseTimeEntity;
import capston.cau.domain.Member;
import capston.cau.domain.Problem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    @Column(length=500,nullable=false)
    private String title;

    @Column(columnDefinition = "TEXT",nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="problem_id")
    private Problem problem;

    @Column(columnDefinition = "integer default 0")
    private int view;

    @OneToMany(mappedBy="post",cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    //수정
    public void update(String title, String content) { this.title = title; this.content = content; }

}
