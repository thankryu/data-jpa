package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import javax.persistence.QueryHint;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    List<Member> findTop3HelloBy();

//    // 관례상 주석처리해도 돌아감. 실무에서 거의 안씀
//    @Query(name = "Member.findByUsername")
    List<Member> findByUsername(@Param("username") String username);

    // 복잡해질 경우 대부분 사용
    // 이름이 없는 네임드 쿼리와 같은 기능
    // 서버 올릴때 오류를 찾을 수 있음.
    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUserNameList();

    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);

    List<Member> findListByUsername(String username); // 컬렉션
    Member findMemberByUsername(String username); // 단건
    Optional<Member> findOptionalByUsername(String username); // 단건 optional

    // 성능이슈가 있을 때는 countQuery 별도로 처리할 수 있다
    // sorting 이 복잡해질 경우 @Query에 sort 직접 하는것이 좋을때도 있다.
    @Query(value = "select m from Member m left join m.team t",
            countQuery = "select count(m) from Member m")
    Page<Member> findByAge(int age, Pageable pageable);
    // Slice<Member> findByAge(int age, Pageable pageable);
    // List<Member> findByAge(int age, Pageable pageable);

    // Modifying 변경시에 필요
    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    // JPQL 방식으로 fetch Join
    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();

    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    @EntityGraph(attributePaths = {"team"})
    List<Member> findEntityGraphByUsername(@Param("username") String username);

    @EntityGraph("Member.all")
    List<Member> findEntityGraphNamedByUsername(@Param("username") String username);

    // 읽기로만 쓰는것으로 최적화가 되어 변경이 안됨
    @QueryHints(value = @QueryHint( name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String username);
}
