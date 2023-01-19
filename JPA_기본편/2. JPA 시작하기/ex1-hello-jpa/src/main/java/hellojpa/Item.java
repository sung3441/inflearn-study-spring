package hellojpa;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn // name default DTYPE
public abstract class Item { // 슈퍼 테이블은 추상 클래스로 생성

    @Id @GeneratedValue
    private Long id;

    private String name;

    private int price;
}
