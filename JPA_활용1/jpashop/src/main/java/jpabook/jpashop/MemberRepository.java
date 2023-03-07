package jpabook.jpashop;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class MemberRepository {

    @PersistenceContext
    private EntityManager em;

    public Member find(Long id) {
        return em.find(Member.class, id);
    }

    public Long save(Member member) {
        em.persist(member);
        return member.getId();
    }
}
