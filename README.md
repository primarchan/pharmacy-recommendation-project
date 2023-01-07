# 약국 길찾기 서비스(Kakao Open API)
<hr>

## 공공기관 데이터 약국 데이터 셋업 (방법 1)
> - https://www.data.go.kr/data/15065023/fileData.do
> - 직접 `SQL` 생성하여 저장하는 방법
> - 도커 컨테이너 생성 시 초기 데이터 만들기
> - <b>디렉토리 `/docker-entrypoint-initdb.d/` 에 `.sql` 또는 `.sh` 파일을 넣어두면 컨테이너 실행 시 실행된다.</b>
<hr>

## 공공기관 데이터 약국 데이터 셋업 (방법 2)
> - https://github.com/WonYong-Jang/Pharmacy-Recommendation
> - 초기 데이터가 `csv` 파일이므로, 로드하여 DB 저장
<hr>

## Docker 세팅 확인
> - `docker-compose -f docker-compose-local.yml up`
> - 터미널 접속 -> `docker ps` -> `docker exec -it { CONTAINER ID } bash`
> - `mysql -u root -p` -> password 입력
> - `show databases;` -> `use pharmacy_recommendation` -> `show tables;` -> `select * from pharmacy;`
<hr>

## Build 가이드
> - MacOS: `./gradlew clean build`
> - Window: `gradlew clean build`
> - 전체 테스트 코드 실행을 위한 API KEY 적용 커맨드
> `.gradlew clean build -PKAKAO_REST_API_KEY={key}`
<hr>

## 거리 계산 알고리즘 구현
> - https://en.wikipedia.org/wiki/Haversine_formula
> - <b>두 위도, 경도 사이의 거리를 계산하기 위한 알고리즘</b>
> - `Haversine formula` 알고리즘은 지구를 완전한 구 라고 가정하고 계산하기 때문에 0.5% 정도 오차가 발생 가능
> - 해당 프로젝트에서 약간의 오차는 이슈가 없기 때문에 아래와 같이 구현
> ```java
> // Haversine formula
> private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
>   lat1 = Math.toRadians(lat1);
>   lon1 = Math.toRadians(lon1);
>   lat2 = Math.toRadians(lat2);
>   lon2 = Math.toRadians(lon2);
> 
>   double earthRadius = 6371;  // Kilometers
>   return earthRadius * Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));
> }
> ```
<hr>

## Spring Retry 소개
> - https://wonyong-jang.github.io/spring/2021/02/18/Spring-Retry.html
> - https://github.com/spring-projects/spring-retry
<hr>

## Shorten URL 개발
> - 주로 모바일 등으로 `URL` 을 공유하고자 할 때, 긴 `URL` 을 줄여서 제공
> - <b>요청 `URL` 에 대응하는 `Unique` 한 식별자가 필요</b>
> 1. `DB` 에 길 안내 정보 (위도, 경도, 약국정보) 를 저장하고, `PK` 를 사용하는 방법  
> ex) `http://localhost:8080/dir/10000000`  
> 10진수 보다 더 간결한 64진수 또는 62진수
> 2. `Base64` 사용  
> "=" 등은 `URL` 예약어이기 때문에 부적절  
> 3. `Base62` 사용  
> ex) `http://localhost:8080/dir/raad21`  
<hr>

## Redis (Remote Dictionary Server) 소개
> - `Redis` 란 오픈소이며, `In-Memory` 데이터베이스로써 다양한 자료구조를 제공
> - `https://redis.io/docs/data-types/tutorial`
> 메모리 접근이 디스크 접근보다 빠르기 때문에 데이터베이스 (MySQL, Oracle...) 보다 빠르다.
> - <b>매번 `Request` 마다 약국 데이터를 `DB` 에서 조회, 거리계산 알고리즘 계산 및 정렬 후 결과값 반환</b>
> - 본 프로젝트에서 현재 약국 도메인 데이터가 크지 않기 때문에 Redis 사용은 Optional
> - 데이터가 큰 경우 이를 매번 DB 조회하는 부분을 메모리에 캐싱하여 성능 향상 가능
> - `Redis` 캐싱을 이용하여 성능을 개선하고자 할 때, <b>캐싱 데이터는 `Update` 가 자주 일어나지 않는 데이터가 효과적</b>
> - 너무 많은 `Update` 가 일어나는 데이터일 경우, `DB` 와의 `Sync` 비용 발생
> - `Redis` 사용 시 반드시 `Failover` 에 대한 고려
> ex) `Redis` 장애 시 데이터베이스에서 조회, `Redis` 이중화 및 백업
<hr>

## Redis (Remote Dictionary Server) CLI
> - <b>Redis CLI</b>  
> `$ docker exec -it {Container ID} redis-cli --raw`  // Redis CLI 로 접속  
> - <b>String 자료 구조 (Key - Value)</b>  
> `$ set id 10`  // Key (id) 의 Value 를 10으로 저장  
> `$ get id`  // Key 조회  
> `$ del id`  // Key 삭제  
> `$ scan 0`  // Key 들을 일정 단위 개수 만큼씩 조회
> - <b>Hash 자료 구조 (Key - subKey - Value)</b>  
> `$ hgetall USER`  // Key (User) 의 매핑되는 모든 필드와 값들을 조회  
> `$ hset USER subkey value`  // Key (User) 의 subKey 의 값을 지정  
> `$ hget USER subkey`  // Key (User) 의 subKey 의 값을 조회  
<hr>

## 프로젝트 관련 학습 자료
<details>
<summary><b>내용 상세 보기 (클릭) </b></summary>
<div markdown="1">

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

### MockMvc 를 이용한 테스트 코드 작성
> - 스프링 MVC 를 모킹하여 웹 어플리케이션을 테스트할 수 있는 도구
> - 컨트롤러 레이어를 테스트 하기 위해 사용
> - 매번 직접 서버를 띄우고 브라우저를 통해서 테스트하지 않고 테스트 코드를 통해 검증 가능
> - [참고자료](https://wonyong-jang.github.io/spring/2022/07/08/Spring-MockMvc.html)
<hr>

</div>
</details>
