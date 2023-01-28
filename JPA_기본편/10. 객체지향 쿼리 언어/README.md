# JPA가 지원하는 쿼리 방법

- JPQL
- JPA Criteria
- QueryDSL
- 네이티브 SQL
- JDBC API 직접 사용, MyBatis, SpringJdbcTemplate 함께 사용

### JPQL

- JPA를 사용하면 엔티티 객체를 중심으로 개발
- 검색
  - 검색을 할 때 테이블이 아닌 엔티티 객체를 대상으로 검색
  - 모든 DB 데이터를 객체로 변환해서 검색하는 것은 불가능
  - 애플리케이션이 필요로 하는 데이터만 DB에서 불러오려면 sql에 검색 조건이 포함되어야 함
- JPA는 SQL을 추상화한 JPQL이라는 객체 지향 쿼리 언어 제공
- SQL과 유사한 문법
  - SELECT, FROM, WHERE, GROUP BY, HAVING, JOIN 지원
- JPQL은 엔티티 객체를 대상으로 쿼리
- sql은 데이터 베이스 테이블을 대상으로 쿼리

```java
// 검색
String jpql = "select m from Member m where m.username like '%cho%'";
List<Member> result = em.createQuery(jpql, Member.class).getResultList*();
```
```sql
// 실제 쿼리
select m from Member m
where m.username like '%cho%'
```

- 테이블이 아닌 객체를 대상으로 검색하는 객체 지향 쿼리
- sql을 추상화해서 특정 데이터베이스 sql에 의존 x
- 객체 지향 sql이라고 정의할 수 있음

### Criteria (거의 사용 x)

- 문자가 아닌 자바코드로 JPQL을 작성할 수 있음
- JPQL 빌더 역할
- JPA 공식 기능
- 복잡하고 실용성 x
- 대신 QueryDSL 사용 권장

```java
// Criteria 사용 준비
CriteriaBuilder cb = em.getCriteriaBuilder();
criteriaQuery<Member> query = cb.createQuery(Member.class);

// root class (조회 시작 클래스)
Root<Member> m = query.from(Member.class);

// 쿼리 생성
CriteriaQuery<Member> cq = query.select(m).where(cb.equal(m.get("username"), "kim"));
List<Member> resultList = em.createQuery(cq).getResultList();

```

### QueryDSL 소개

- 문자가 아닌 자바코드로 JPQL을 작성할 수 있음
- JPQL 빌더 역할
- 컴파일 시점에 문법 오류 찾을 수 있음
- 동적쿼리 작성 편리
- 단순하고 쉬움

### 네이티브 SQL 소개

- JPA가 제공하는 SQL을 직접 사용하는 기능
- JPQL로 해결할 수 없는 특정 데이터베이스에 의존적인 기능
- ex) 오라클 CONNECT BY, 특정 DB만 사용하는 SQL 힌트

```java
String sql = "SELECT ID, AGE, TEAM_ID, NAME FROM MEMBER WHERE NAME = 'CHO'";

Lisg<Member> resultList = em.createNativeQuery(sql, Member.class).getResultList();
```

### JDBC 직접 사용, SpringJdbcTemplate 등

- JPA를 사용하면서 JDBC 커넥션을 직접 사용하거나, 스프링 JdbcTemplate, Mybatis 등을 함께 사용 가능
- 단 영속성 컨텍스트를 적절한 시점에 강제로 플러시 필요
