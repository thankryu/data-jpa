package study.datajpa.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import study.datajpa.entity.Member;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * 임의의 repository 생성도 사용가능 예제
 */
@Repository
@RequiredArgsConstructor
public class MemberQueryRepository {

    private final EntityManager em;
    List<Member> findAllMembers(){
        return em.createQuery("select m from Member m")
                .getResultList();
    }
}
