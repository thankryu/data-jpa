package study.datajpa.repository;

import org.springframework.beans.factory.annotation.Value;

/**
 * 인터페이스 기반 Open Projections
 */
public interface UsernameOnly {
    
    // Value 사용시 오픈 프로젝션 원하는 대로 만들어주기 -> 최적화는 안됨
    @Value("#{target.username + ' ' + target.age + ' ' + target.team.name}")
    String getUsername();

    // close 프로젝션 최적화용도
    // String getUsername();
}