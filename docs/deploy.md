# 🗄️ Database Deployment Guide
```bash
[마스터 노드]
# 1. NFS 서버 패키지 설치
sudo apt update && sudo apt install -y nfs-kernel-server

# 2. 공유할 통합 폴더 생성 및 권한 개방
sudo mkdir -p /srv/nfs/mariadb
sudo chown -R 999:999 /srv/nfs/mariadb
sudo chmod -R 777 /srv/nfs/mariadb

# 3. 5개 DB를 위한 하위 폴더 미리 생성
sudo mkdir -p /srv/nfs/mariadb/command-db \
             /srv/nfs/mariadb/query-db \
             /srv/nfs/mariadb/vehicle-db \
             /srv/nfs/mariadb/zone-db \
             /srv/nfs/mariadb/orchestration-db

# 4. NFS 설정 파일(/etc/exports)에 공유 대역 등록
echo "/srv/nfs/mariadb 10.0.2.0/24(rw,sync,no_subtree_check,no_root_squash)" | sudo tee -a /etc/exports

# 5. 설정 적용 및 서버 재시작
sudo exportfs -ra
sudo systemctl restart nfs-kernel-server

[워커 노드]
# 6. 워커노드 (모든 워커 노드에서 실행)
sudo apt update && sudo apt install -y nfs-common
sudo apt update && sudo apt install -y pigz
[마스터 노드]
# 7. 배포 
kubectl apply -f pv.yaml
kubectl apply -f parking-command-db.yaml -f parking-query-db.yaml -f vehicle-db.yaml -f zone-db.yaml -f orchestration-db.yaml
kubectl apply -f migrate-jobs.yaml
```
# 🗄️ Backend Deployment Guide
```bash

# 1. 배포
cd ~/Deploy/backend
kubectl apply -f zone-grpc.yaml -f vehicle-grpc.yaml -f parking-query-grpc.yaml -f parking-command-grpc.yaml
kubectl apply -f orchestration-http.yaml
```
# 🗄️ Frontend Deployment Guide

```bash
# 1. 배포
cd ~/Deploy/frontend
kubectl apply -f deployment.yaml
```
# 🗄️ Parking-Bot Deployment Guide
```bash
# 1. 배포
#사전 데이터 주입 필요 : Deploy/docs/setup_Parking-bot.md
cd ~/Deploy/database
kubectl apply -f bot-deployment.yaml
```
