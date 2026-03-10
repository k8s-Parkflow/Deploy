**1. 설정 파일 덮어쓰기 (Overwrite Configuration)**
BE 디렉토리의 설정 파일들을 park_python/park_py 경로의 기존 파일들에 덮어씌웁니다.

settings.py: BE/settings.py → park_python/park_py/settings.py

urls.py: BE/urls.py → park_python/park_py/urls.py

**2. DB 라우터 파일 추가 (Add DB Router)**
4개의 데이터베이스(command, query, vehicle, zone)로 요청을 분산하기 위해 db_router.py 파일을 추가해야 합니다.

위치: BE/db_router.py 파일을 park_python/park_py/ 디렉토리 안으로 복사합니다.

**3. nginx.conf 파일 이동**

위치 : nginx.conf파일을 ParkFlow 디렉토리 안으로 복사
