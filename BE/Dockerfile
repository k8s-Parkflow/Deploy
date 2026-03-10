# 1단계: 빌드 스테이지 (의존성 설치)
FROM python:3.11-slim AS builder
WORKDIR /app

# 환경변수 설정 (파이썬 로그 즉시 출력 및 바이트코드 생성 방지)
ENV PYTHONDONTWRITEBYTECODE 1
ENV PYTHONUNBUFFERED 1

# [수정] mysqlclient 빌드를 위한 필수 시스템 라이브러리 설치
RUN apt-get update && apt-get install -y \
    gcc \
    default-libmysqlclient-dev \
    pkg-config \
    && rm -rf /var/lib/apt/lists/*

# 가상환경 생성 및 의존성 설치
RUN python -m venv /opt/venv
ENV PATH="/opt/venv/bin:$PATH"

# requirements.txt가 있다면 복사 (없다면 아래 RUN 명령어로 직접 설치 가능)
# 현재 프로젝트에 없다면 아래 RUN 명령어를 그대로 사용하세요.
RUN pip install --upgrade pip
RUN pip install django mysqlclient

# 2단계: 실행 스테이지 (최종 경량 이미지)
FROM python:3.11-slim
WORKDIR /app

# 실행에 필요한 최소한의 시스템 라이브러리만 설치
RUN apt-get update && apt-get install -y \
    default-libmysqlclient-dev \
    && rm -rf /var/lib/apt/lists/*

# 빌드 스테이지에서 설치된 가상환경 통째로 복사
COPY --from=builder /opt/venv /opt/venv
ENV PATH="/opt/venv/bin:$PATH"

# 프로젝트 전체 코드 복사
COPY . .

# Django 기본 포트
EXPOSE 8000

# 앱 실행 (manage.py 위치 기준)
# Gunicorn 같은 서버를 쓰지 않고 일단 runserver로 실행하는 설정입니다.
ENTRYPOINT ["python", "manage.py", "runserver", "0.0.0.0:8000"]