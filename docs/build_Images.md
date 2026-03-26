# 🐳 Docker Image Build & Push Guide

이 문서는 백엔드, 프론트엔드, 그리고 파킹 봇의 도커 이미지를 빌드하고 레지스트리에 푸시하는 절차를 기록합니다.

### 전체 빌드 및 배포 스크립트

# [Backend 빌드 및 푸시]
```bash
cd ~/park_python

docker build -t hyungdongjo/parking-orchestration-app:latest -f services/orchestration-service/Dockerfile .
docker build -t hyungdongjo/parking-vehicle-app:latest -f services/vehicle-service/Dockerfile .
docker build -t hyungdongjo/parking-zone-app:latest -f services/zone-service/Dockerfile .
docker build -t hyungdongjo/parking-command-app:latest -f services/parking-command-service/Dockerfile .
docker build -t hyungdongjo/parking-query-app:latest -f services/parking-query-service/Dockerfile .

docker push hyungdongjo/parking-orchestration-app:latest 
docker push hyungdongjo/parking-vehicle-app:latest 
docker push hyungdongjo/parking-zone-app:latest 
docker push hyungdongjo/parking-command-app:latest 
docker push hyungdongjo/parking-query-app:latest

docker pull hyungdongjo/parking-orchestration-app:latest 
docker pull hyungdongjo/parking-vehicle-app:latest 
docker pull hyungdongjo/parking-zone-app:latest 
docker pull hyungdongjo/parking-command-app:latest 
docker pull hyungdongjo/parking-query-app:latest
```
# [Frontend 빌드 및 푸시]
```bash
cd ~/ParkFlow

docker build -t hyungdongjo/parking-frontend:latest .  
docker push hyungdongjo/parking-frontend:latest
docker pull hyungdongjo/parking-frontend:latest
```
# [Parking-Bot 빌드 및 푸시]
```bash
cd ~/parking-bot

docker build -t hyungdongjo/parking-bot:latest .  
docker push hyungdongjo/parking-bot:latest
docker pull hyungdongjo/parking-bot:latest
```
