package jpql;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Team team = new Team();
            team.setName("Team A");
            em.persist(team);

            Team team2 = new Team();
            team2.setName("Team B");
            em.persist(team2);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.changeTeam(team);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.changeTeam(team);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.changeTeam(team2);
            em.persist(member3);

            em.flush();
            em.clear();

            String query = "select t from Team t join fetch t.members";

            // 이 시점에 Team은 프록시 객체가 아니라 엔티티가 담기게 됨, 데이터 전부 채워져 있음
            List<Team> resultList = em.createQuery(query, Team.class)
                    .getResultList();

            for (Team t : resultList) {
                System.out.println("t.getName() = " + t.getName());
                for (Member m : t.getMembers()) {
                    System.out.println("m.getUsername() = " + m.getUsername());
                }
            }

            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }
}
