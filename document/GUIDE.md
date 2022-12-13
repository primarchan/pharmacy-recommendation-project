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