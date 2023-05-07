package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamRepository teamRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    public void testMember(){
        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member);

        // test 를 위해 get 을 사용
        // 원래는 Optional 로 받음
        Member findMember = memberRepository.findById(savedMember.getId()).get();

        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void basicCRUD(){
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");

        memberRepository.save(member1);
        memberRepository.save(member2);

        // 단건 조회 검증
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();

        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        // 상태 변경
        findMember1.setUsername("member!!!!!");

        // 리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long deleteCount = memberRepository.count();
        assertThat(deleteCount).isEqualTo(0);
    }

    @Test
    public void findByUsernameAndAgeGreaterThen(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("AAA", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);
        assertThat(result.get(0).getUsername()).isEqualTo("aaa");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void findHelloBy(){
        List<Member> helloBy = memberRepository.findTop3HelloBy();
    }

    @Test
    public void testNameQuery(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsername("AAA");
        Member findMember = result.get(0);
        assertThat(findMember).isEqualTo(m1);
    }

    @Test
    public void testQuery(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findUser("AAA", 10);
        assertThat(result.get(0)).isEqualTo(m1);
    }

    @Test
    public void findUsernameList(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<String> userNameList = memberRepository.findUserNameList();
        for(String s : userNameList){
            System.out.println("S = "+s);
        }
    }

    @Test
    public void findMemberDto(){
        Team team = new Team("teamA");
        teamRepository.save(team);

        Member m1 = new Member("AAA", 10);
        m1.setTeam(team);
        memberRepository.save(m1);

        List<MemberDto> memberDto = memberRepository.findMemberDto();
        for(MemberDto dto : memberDto){
            System.out.println("dto = "+dto);
        }
    }

    @Test
    public void findByNames(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));
        for(Member member : result){
            System.out.println("member = "+member);
        }
    }

    @Test
    public void returnType(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);
        
        // 컬렉션 조회 => 특이사항 null 이 아님. 없을떄 empty 반환
        List<Member> aaa = memberRepository.findListByUsername("AAA");
        
        // 단건 조회 => 없으면 null 이 반환
        Member findMember = memberRepository.findMemberByUsername("AAA");
        System.out.println("findMember = "+findMember);

        // 단건 optional 조회 : 권장
        Optional<Member> optionalMember = memberRepository.findOptionalByUsername("AAA");
        System.out.println("findMember Optional = "+optionalMember);
    }

    @Test
    public void paging(){
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        Page<Member> page = memberRepository.findByAge(age, pageRequest);
//        Slice<Member> page = memberRepository.findByAge(age, pageRequest);
//        List<Member> page = memberRepository.findByAge(age, pageRequest);

        // Jpa 데이터 전환! -> 화면엔 DTO 로 넘겨야 한다!
        Page<MemberDto> toMap = page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));
        System.out.println("toMap = "+toMap.getContent());

        List<Member> content = page.getContent();

        assertThat(content.size()).isEqualTo(3);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();

        // Page 에서만 가능
        assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.getTotalPages()).isEqualTo(2);

    }

    @Test
    public void bulkUpdate(){
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 21));
        memberRepository.save(new Member("member5", 40));

        // jpql 을 적으면 DB에 적용하고 jpql 을 적용한다 순서 주의
        // bulk 연산은 DB에 강제로 집어넣음 : 영속성 컨텍스트 충돌이 우려된다
        int resultCount = memberRepository.bulkAgePlus(20);
        
        // 아래 작업들로 영속성 컨텍스트를 날리고 해야 제대로 된 값을 얻을 수 있다
        // @Modifying(clearAutomatically = true) 일때는 하단 내용을 자동으로 처리해 준다
//        em.flush();
//        em.clear();

        List<Member> result = memberRepository.findByUsername("member5");

        Member member5 = result.get(0);
        System.out.println("member5::"+member5);

        assertThat(resultCount).isEqualTo(3);
    }

    @Test
    public void findMemberLazy(){

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 10, teamB);

        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        // when N + 1
        // select Member 1
        // List<Member> members = memberRepository.findAll(); // 변경 전
        // 해결책 fetch join 적용 = 특이점 + join + select 절에 자동적으로 추가를 함
        // List<Member> members = memberRepository.findMemberFetchJoin();

        // @EntityGraph 적용
        List<Member> members = memberRepository.findAll();

        for (Member member : members) {
            System.out.println("member = "+member.getUsername());
            System.out.println("member.teamClass = "+member.getTeam().getClass());
            System.out.println("member.team = "+member.getTeam().getName());
        }

        // 많이 씀
        System.out.println("-------------------------EntityGraph + Where-------------------------");
        List<Member> members2 = memberRepository.findEntityGraphByUsername("member1");

        for (Member member : members2) {
            System.out.println("member = "+member.getUsername());
            System.out.println("member.teamClass = "+member.getTeam().getClass());
            System.out.println("member.team = "+member.getTeam().getName());
        }

        // 많이 안씀
        System.out.println("-------------------------NamedEntityGraph-------------------------");
        List<Member> members3 = memberRepository.findEntityGraphNamedByUsername("member1");

        for (Member member : members3) {
            System.out.println("member = "+member.getUsername());
            System.out.println("member.teamClass = "+member.getTeam().getClass());
            System.out.println("member.team = "+member.getTeam().getName());
        }

    }

    @Test
    public void queryHint(){
        // given
        Member member1 = memberRepository.save(new Member("member1", 10));
        em.flush();
        em.clear();

        // when
        // 읽기 최적화만 되어 사용, 변경 감지 사용안됨
        Member findMember = memberRepository.findReadOnlyByUsername("member1");
        findMember.setUsername("member2");

        em.flush();
    }
}


















