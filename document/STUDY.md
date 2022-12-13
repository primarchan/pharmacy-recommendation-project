### Spring Data JPA 란?
> - JPA 란 Java 어플리케이션에서 관계형 데이터베이스를 사용하는 방식을 정의한 인터페이스
> - Spring Data JPA 는 Spring 에서 제공하는 모듈 중 하나로, 개발자가 JPA 를 더 쉽고 편하게 사용할 수 있도록 도와준다.
> - <b>JPA 를 한 단계 추상화시킨 Repository 인터페이스 제공</b>
> - build.gradle 의존성 추가 및 인터페이스만 정의해주게 되면 JPA 의 CRUD 를 바로 사용 가능
> - <b>단, 영속성 컨텍스트 및 Dirty Checking 개념을 잘 이해하고 사용하지 않으면 데이터 손실 및 성능 이슈가 발생할 수 있다.</b>
<hr>

### Spring Data JPA 사용 시 주의사항
> - <b>`JPA` 의 모든 데이터 변경은 아래와 같이 트랜잭션 안에서 실행된다.</b>
> - <b>즉, 트랜잭션 밖에서 데이터 변경은 반영되지 않는다.</b>
> - `Spring Data JPA` 구현 코드를 살펴보면, 변경이 일어나는 코드는 `@Transactional` 이 이미 추가되어 있다.
> - 즉, 구현 코드를 정확히 이해하지 않고 사용시 문제가 발생할 수 있다.
<hr>

### 영속성 컨텍스트 (Persistence Context)
> - 영속성 컨텍스트는 엔티티를 저장하고 관리하는 저장소이며, 어플리케이션과 데이터베이스 사이에 entity 를 보관하는 가상의 데이터베이스 같은 역할
> - <b>`Spring Data JPA` 에서 제공하는 `save` 메서드 구현 코드를 보면 `em.persist` 를 통해 영속성 컨텍스트에 저장</b>
> - <b>이때, 엔티티는 영속상태</b>
> - 이미 영속상태인 경우 `merge` 를 통해 덮어쓴다.
<hr>

### 영속성 컨텍스트를 왜 사용할까?
> - Database 와 어플리케이션 사이의 중간 계층에 있으면서 여러가지 이점이 있다.
> - 영속성 컨텍스트 내에 1차 캐시
> - 영속성 컨텍스트 내에 쓰기 지연 SQL 저장소
> - 엔티티 수정 (`Dirty Checking`)
<hr>

### 영속성 컨텍스트 - 1차 캐시
> - 영속성 컨텍스트 내부에 1차 캐시를 가지고 있다.
> - persist 를 하는 순간 PK 값 (ID), 타입과 객체를 맵핑하여 1차 캐시에 가지고 있음
> - <b>한 트랜잭션 내에 1차 캐시에 이미있는 값을 조회하는 경우, DB 를 조회하지 않고 1차 캐시에 있는 내용을 그대로 가져온다.</b>
> - 단, 1차 캐시는 어플리케이션 전체 공유가 아닌 한 트랜잭션 내에서만 공유
> - 반면, 조회했을 때 1차 캐시에 없다면 DB 에서 가져와서 1차 캐시에 저장 후 반환
> ```Java
> // 엔티티를 생성한 상태 (비영속)
> Member member = new Member();
> member.setId("member1");
> member.setUsername("회원1");
> 
> // 엔티티를 영속
> em.persist();
> ```
<hr>

### 영속성 컨텍스트 - 쓰기 지연 SQL
> - `memberA` 를 `persist` 하는 순간, 1차 캐시에 넣고 쓰기를 지연 SQL 저장소에 쿼리를 만들어 쌓는다.
> - `memberB` 도 `persist` 하는 순간, 동일한 과정을 거치며, <b>`commit` 하는 순간 `flush` 가 되면서 DB 에 반영</b>
> - `flush` 란 영속성 컨텍스트의 변경 내용을 DB 에 반영하며, 1차 캐시를 지우지는 않는다.

### JPA Dirty Checking
> - 코드에서 엔티티의 값만 변경했을 뿐인데, 데이터베이스 업데이트 쿼리가 발생한다?
> - 이유는 `Dirty Checking` 덕분이며, `Dirty` 란 상태의 변화가 생긴 정도를 의미한다.
> - <b>즉, `Dirty Checking` 이란 엔티티 상태 변경 검사</b>
> - JPA 에서 트랜잭션이 끝나는 시점에 변화가 있는 모든 entity 객체를 데이터베이스에 자동으로 반여해 준다.
<hr>

### Dirty Checking 내부 구조
> - `JPA` 는 `commit` 하는 순간 내부적으로 `flush` 가 호출되고, 이 때 엔티티와 스냅샷을 비교
> - 1차 캐시에는 처음 들어온 상태인 엔티티 스냅샷을 넣어두고 commit 하는 순간 변경된 값이 있는지 비교하여 변경된 값이 있으면 `update`쿼리를 쓰기 지연 SQL에 넣어둔다. 
<hr>

### JPA Dirty Checking 주의사항
> - 당연히 Dirty Checking 은 영속성 컨텍스트가 관리하는 entity 에만 적용된다.
> - <b>영속성 컨텍스트에 처음 저장된 순간 스냅샷을 저장해놓고, 트랜잭션이 끝나는 시점에 비교하여 변경된 부분을 쿼리로 새성하여 데이터베이스로 반영</b> 한다.
> - <b>즉, 영속 상태가 아닐 경우, 값을 변경해도 데이터베이스에 반영되지 않는다.</b>
> - <b>트랜잭션이 없이 데이터 반영이 일어나지 않는다.</b>
<hr>

### Spring Transactional 이란?
> - `Spring` 에서 `Transaction` 처리를 `@Transactional` 어노테이션을 이용하여 처리
> - `@Transactional` 은 `Spring AOP` 기반이며, `Spring AOP` 는 `Proxy` 기반으로 동작
> - <b>`@Transactional` 이 포함된 메서드가 호출될 경우, `Proxy` 객체를 생성함으로써 `Transaction` 생성 및 커밋 또는 롤백 후 `Transaction` 닫는 부수적인 작업을 `Proxy` 객체에게 위임</b>
> - `Proxy` 의 핵심적인 기능은 지정된 메서드 호출(`Invocation`) 될 때, 이 메서드를 가로채어 부가 기능들을 `Proxy` 객체에게 위임
> - 즉, 개발자가 메서드에 `@Transactional` 만 선언하고, 비지니스 로직에 집중 가능!
<hr>

### Spring Transactional 주의사항
> - https://tedblob.com/spring-aop-proxy
> - Spring `AOP` 기반으로 하는 기능들 (`@Transactional`, `@Cacheable`, `@Async`) 사용 시 `Self Invocation` 문제로 인하여 장애가 발생할 수 있음
> - 메서드가 호출되는 시점에 프록시 객체를 생성하고, 프록시 객체는 부가 기능 (`Transaction`) 을 주입해 준다.
> - 외부에서 `bar()` 메서드를 실행할 때 정상적으로 프록시가 동작
> - 하지만, `@Transactional` 을 `foo()` 에만 선언하고 외부에서 `bar()` 를 호출하고, `bar() -> foo()` 호출했다면?
<hr>

### Self Invocation 해결 방법
> - 트랜잭션 위치를 외부에서 호출하는 `bar()` 메서드로 이동
> - 객체의 책임을 최대한 분리하여 외부 호출하도록 리팩토링
<hr>

### Spring Transaction 주의사항 (읽기 전용)
> - `@Transactional(readOnly - true)` 스프링에서 트랜잭션을 읽기 전용으로 설정 가능
> - 읽기 전용으로 설정하게 되면, JPA 에서 스냅샷 저장 및 `Dirty Checking` 작업을 수행하지 않기 때문에 성능적으로 이점
> - 따라서, `Dirty Checking` 불가
<hr>

### Spring Transactional 주의사항 (우선순위)
> - `@Transactional` 은 적용 우선순위를 가지고 있으며, 클래스 보다 메서드가 우선순위가 높다
> - 클래스에 `@Transactional(readOnly = true)` (읽기전용) 으로 적용해 놓고, `update` 가 발생하는 메서드에만 `readOnly = false` 우선 적용 (`SimpleJpaRepository`)
<hr>

