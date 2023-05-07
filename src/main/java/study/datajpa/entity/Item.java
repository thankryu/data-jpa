package study.datajpa.entity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item implements Persistable<String> {

    @Id
    private String id;

    @CreatedDate
    private LocalDateTime createdDate;

    public Item(String id){
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    /**
     * 기존에는 신규로 추가할 때 @Id가 null 이 아닐때
     * select + insert 가 동작하여 추가적인 sql 접속이 발생 
     * Persistable 를 사용하여 새로운 객체인지 판단 
     * createdDate 를 사용하여 새로운 객체인지 아닌지 판단 권장
     */
    @Override
    public boolean isNew() {
        return createdDate == null;
    }
}