package study.datajpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

import javax.annotation.PostConstruct;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id){
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    /**
     * 도메인 클래스 컨버터 사용
     * 조회용으로만 사용 가능
     * 간단할 경우에는 사용할 수 있다
     * @param member
     * @return
     */
    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable("id") Member member){
        return member.getUsername();
    }
    
//   // Entity를 그대로 노출하는 경우 -> 비추천
//    @GetMapping("/members")
//    public Page<Member> list(@PageableDefault(size = 5) Pageable pageable){
//        Page<Member> page = memberRepository.findAll(pageable);
//        return page;
//    }

    @GetMapping("/members")
    public Page<MemberDto> list(@PageableDefault(size = 5) Pageable pageable){
        // 1. pageable 로 entity 값 조회
        // 2. entity 값 -> Dto 에 담아서 반환
        // 2-1. 방법1
//        return memberRepository.findAll(pageable)
//                .map(member -> new MemberDto(member.getId(), member.getUsername(), null));

        // 2-2. 방법2 Dto 에서 선언 후 가능
        return memberRepository.findAll(pageable)
                .map(MemberDto::new);
    }


    @PostConstruct
    public void init(){
        for(int i =0; i < 100; i++){
            memberRepository.save(new Member("user" + i, i));
        }
    }
}
