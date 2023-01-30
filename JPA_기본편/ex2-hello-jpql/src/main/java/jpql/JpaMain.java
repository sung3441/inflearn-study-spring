package jpql;

import javax.persistence.*;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Member member = new Member();
            member.setUsername("member");
            member.setAge(30);
            em.persist(member);

            List<Team> result1 = em.createQuery("select t from Member m join m.team t", Team.class)
                    .getResultList();

            // 여러 값 조회
            List<MemberDto> result2 = em.createQuery("select new jpql.MemberDto(m.username, m.age) from Member m", MemberDto.class)
                    .getResultList();

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
