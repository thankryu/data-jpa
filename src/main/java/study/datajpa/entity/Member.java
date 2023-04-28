package study.datajpa.entity;

import lombok.Getter;
import lombok.Setter;
import study.datajpa.repository.MemberJpaRepository;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class Member {

    @Id
    @GeneratedValue
    private Long id;
    private String username;

    // 프록시 기술을 쓸때 기본적으로 필요함 private 쓰면안됨
    protected Member() {

    }

    public Member(String username) {
        this.username = username;
    }
}