package study.datajpa.repository;

import lombok.RequiredArgsConstructor;
import study.datajpa.entity.Member;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * MemberRepository + Impl 명명규칙 확인해야함
 * Spring Data 인터페이스만으로 해결이 안될때 사용함
 * 복잡한 동적 쿼리
 * ex) query dsl, mybatis 등
 */
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom{

    private final EntityManager em;

    @Override
    public List<Member> findMemberCustom() {
        return em.createQuery("select m from Member m")
                .getResultList();
    }
}
