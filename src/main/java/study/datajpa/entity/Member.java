package study.datajpa.entity;

import lombok.*;
import study.datajpa.repository.MemberJpaRepository;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "username", "age"}) // team 쓰면 큰일남 무한루프: 연관관계가 있을떄 ToString 쓰지 말자
@NamedQuery(
        name ="Member.findByUsername",
        query = "select m from Member m where m.username =:username"
) // 실무에서 많이 않씀 :: 장점 문법 오류를 잡아줌
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String username;
    private int age;

    // lazy로딩 신경쓰자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

//    // 프록시 기술을 쓸때 기본적으로 필요함 private 쓰면안됨
//    // NoArgsConstructor 로 바꿔 쓸 수 있음
//    protected Member() {
//
//    }

    public Member(String username) {
        this.username = username;
    }

    public Member(String username, int age, Team team) {
        this.username = username;
        this.age = age;
        if(team != null ){
            changeTeam(team);
        }
    }

    public Member(String username, int age) {
        this.username = username;
        this.age = age;
    }

    // 연관관계 셋팅
    public void changeTeam(Team team){
        this.team = team;
        team.getMembers().add(this);
    }
}