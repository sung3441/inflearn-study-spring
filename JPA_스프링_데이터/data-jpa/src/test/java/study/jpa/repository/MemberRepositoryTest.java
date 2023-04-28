package study.jpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.jpa.dto.MemberDto;
import study.jpa.entity.Member;
import study.jpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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
    public void testMember() {
        Member member = new Member("member A");
        memberRepository.save(member);

        Member findMember = memberRepository.findById(member.getId()).get();

        assertEquals(findMember.getId(), member.getId());
        assertEquals(findMember.getUsername(), member.getUsername());
        assertEquals(findMember, member);

    }

    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");

        memberRepository.save(member1);
        memberRepository.save(member2);

        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();

        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        memberRepository.delete(member1);
        memberRepository.delete(member2);

        long deletedCount = memberRepository.count();
        assertThat(deletedCount).isEqualTo(0);
    }

    @Test
    public void findByUsernameAndAgeGreaterThen() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("BBB", 15);
        System.out.println(result.size());
        Member findMember = result.get(0);

        assertThat(findMember.getUsername()).isEqualTo("tee");
        assertThat(findMember.getAge()).isEqualTo(20);
    }

    @Test
    public void testNamedQuery() {
        Member m1 = new Member("AAA", 10);

        memberRepository.save(m1);

        List<Member> result = memberRepository.findByUsername("AAA");
        Member findMember = result.get(0);

        assertThat(findMember.getUsername()).isEqualTo("AAA");
        assertThat(findMember.getAge()).isEqualTo(10);
    }

    @Test
    public void testQueryAnnotation() {
        Member m1 = new Member("AAA", 10);

        memberRepository.save(m1);

        List<Member> result = memberRepository.findMember("AAA", 10);
        assertThat(result.get(0)).isEqualTo(m1);
    }

    @Test
    public void findUsernameList() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<String> usernameList = memberRepository.findUsernameList();
        for (String username : usernameList) {
            System.out.println("username = " + username);
        }
    }

    @Test
    public void findMemberDto() {
        Team team = new Team("teamA");
        teamRepository.save(team);


        Member m1 = new Member("AAA", 10);
        m1.changeTeam(team);
        memberRepository.save(m1);


        List<MemberDto> memberDto = memberRepository.findMemberDto();
        for (MemberDto dto : memberDto) {
            System.out.println("dto = " + dto);
        }
    }

    @Test
    public void findByNames() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> byNames = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));
        for (Member byName : byNames) {
            System.out.println("byName = " + byName);
        }
    }

    @Test
    public void returnType() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> listResult = memberRepository.findListByUsername("AAA");
        List<Member> memberResult = memberRepository.findByUsername("AAA");
        Optional<Member> optionalResult = memberRepository.findOptionalByUsername("AAA");
    }

    @Test
    public void paging() {
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        // 반환 타입을 Page로 받을 시 totalCount를 얻을 수 있음
        Page<Member> page = memberRepository.findByAge(age, pageRequest);

        Page<MemberDto> memberDtos = page.map(m -> new MemberDto(m.getId(), m.getUsername(), null));

        assertThat(page.getContent().size()).isEqualTo(3); // 요번 페이징 처리에 가져온 데이터 수
        assertThat(page.getTotalElements()).isEqualTo(5); // 전체 데이터 수
        assertThat(page.getNumber()).isEqualTo(0); // 현재 페이지
        assertThat(page.getTotalPages()).isEqualTo(2); // 총 페이지 수
        assertThat(page.isFirst()).isTrue(); // 현재 첫 페이지인지
        assertThat(page.hasNext()).isTrue(); // 다음 페이지가 있는지
    }

    @Test
    public void bulkUpdate() {
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 15));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 25));
        memberRepository.save(new Member("member5", 30));

        int updateCount = memberRepository.bulkAgePlus(25);

        Member member5 = memberRepository.findByUsername("member5").get(0);
        System.out.println("member5 = " + member5);

        assertThat(updateCount).isEqualTo(2);
    }

    @Test
    public void findMemberLazy() {
        // member1 -> teamA
        // member2 -> teamB

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        em.flush();
        em.clear();

        List<Member> members = memberRepository.findEntityGraphByUsername("member1");
        for (Member member : members) {
            System.out.println("member = " + member.getUsername());
            System.out.println("member.getTeam().getName() = " + member.getTeam().getName());
        }
    }

    @Test
    public void queryHint() {

        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        Member findMember = memberRepository.findReadOnlyByUsername(member1.getUsername());
        findMember.setUsername("member2");

        em.flush();
    }

    @Test
    public void lock() {

        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        List<Member> members = memberRepository.findLockByUsername(member1.getUsername());
    }

    @Test
    public void callCustom() {
        List<Member> members = memberRepository.findMemberCustom();
    }
}