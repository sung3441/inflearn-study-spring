# SQL 중심적인 개발의 문제점

1. 무한 반복 되는 __CRUD__ 코드
2. 유지보수 어려움
3. 패러다임의 불일치 (객체와 관계형 데이터베이스의 차이)
   1. 상속
   2. 연관 관계
      - 객체 = 참조: member.getTeam()
      - 테이블 = 외래 키: JOIN ON M.TEAM_ID = T.TEAM_ID
      - 데이터베이스의 경우 연관 되어있는 테이블이 많을 수록 조회하기가 힘들다. 
        - 어마어마한 양의 join 문..
   3. 데이터 타입
   4. 데이터 식별 방법

#### 테이블에 맞춘 모델링

```java
class Member {
    String id;       // MEMBER_ID 컬럼 사용
    Long teamId;     // TEAM_ID FK 컬럼 사용
    String username; // USERNAME 컬럼 사용
}

class Team {
    Long id;         // TEAM_ID PK 사용
    String name;     // NAME 컬럼 사용
}
```

#### 객체다운 모델링

```java
class Member {
    String id;       // MEMBER_ID 컬럼 사용
    Team team;       // 참조로 연관관계
    String username; // USERNAME 컬럼 사용
    
    Team getTeam() {
        return team;
    }
}

class Team {
    Long id;         // TEAM_ID PK 사용
    String name;     // NAME 컬럼 사용
}
```

