### 공공기관 데이터 약국 데이터 셋업 (방법 1)
> - https://www.data.go.kr/data/15065023/fileData.do
> - 직접 `SQL` 생성하여 저장하는 방법
> - 도커 컨테이너 생성 시 초기 데이터 만들기
> - <b>디렉토리 `/docker-entrypoint-initdb.d/` 에 `.sql` 또는 `.sh` 파일을 넣어두면 컨테이너 실행 시 실행된다.</b>