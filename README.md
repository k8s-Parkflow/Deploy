## NFS 이용
# [마스터 노드]
# 1. NFS 서버 패키지 설치
sudo apt update && sudo apt install -y nfs-kernel-server

# 2. 공유할 통합 폴더 생성 및 권한 개방
sudo mkdir -p /srv/nfs/mariadb
sudo chown -R 999:999 /srv/nfs/mariadb
sudo chmod -R 777 /srv/nfs/mariadb

# 4. NFS 설정 파일(/etc/exports)에 공유 대역 등록
echo "/srv/nfs/mariadb 10.0.2.0/24(rw,sync,no_subtree_check,no_root_squash)" | sudo tee -a /etc/exports

# 5. 설정 적용 및 서버 재시작
sudo exportfs -ra
sudo systemctl restart nfs-kernel-server

# [워커노드]
sudo apt update && sudo apt install -y nfs-common

## 📁 폴더 구조
```
.
├── frontend/
│   └── deployment.yaml
├── backend/
│   ├── deployment-blue.yaml
│   ├── deployment-green.yaml
│   ├── hpa.yaml
│   ├── orchestration-deployment.yaml (API gateway)
│   ├── command-deployment.yaml
│   ├── query-deployment.yaml
│   ├── vehicle-deployment.yaml
│   ├── zone-deployment.yaml
│   └── service.yaml
├── database/
│   ├── command-ss.yaml
│   ├── query-ss.yaml
│   ├── vehicle-ss.yaml
│   ├── zone-ss.yaml
│   ├── configmap.yaml
│   ├── pv.yaml
│   └── service.yaml
└── base/
    ├── ingress.yaml
    └── secret.yaml
```
