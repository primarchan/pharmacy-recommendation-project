### 공공기관 데이터 약국 데이터 셋업 (방법 1)
> - https://www.data.go.kr/data/15065023/fileData.do
> - 직접 `SQL` 생성하여 저장하는 방법
> - 도커 컨테이너 생성 시 초기 데이터 만들기
> - <b>디렉토리 `/docker-entrypoint-initdb.d/` 에 `.sql` 또는 `.sh` 파일을 넣어두면 컨테이너 실행 시 실행된다.</b>
<hr>

### 공공기관 데이터 약국 데이터 셋업 (방법 2)
> - https://github.com/WonYong-Jang/Pharmacy-Recommendation
> - 초기 데이터가 `csv` 파일이므로, 로드하여 DB 저장
<hr>

### Docker 세팅 확인
> - `docker-compose -f docker-compose-local.yml up`
> - 터미널 접속 -> `docker ps` -> `docker exec -it { CONTAINER ID } bash`
> - `mysql uroot -p` -> password 입력
> - `show databases;` -> `use pharmacy-recommendation` -> `show tables;` -> `select * from pharmacy;`
<hr>

### 거리 계산 알고리즘 구현
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

### Spring Retry 소개
> - https://wonyong-jang.github.io/spring/2021/02/18/Spring-Retry.html
> - https://github.com/spring-projects/spring-retry