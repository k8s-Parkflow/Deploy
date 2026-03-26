## 빌드 및 배포 순서
# Deploy/docs 
```bash
1. build_Images 
2. deploy (단, parking-bot 배포는 setup_Parking-Bot 수행 이후)
3. setup_Parking-Bot
4. test_Parking-Bot 
```

## 📁 폴더 구조
```
Deploy
├── backend
│   ├── orchestration-http.yaml
│   ├── parking-command-grpc.yaml
│   ├── parking-query-grpc.yaml
│   ├── vehicle-grpc.yaml
│   └── zone-grpc.yaml
├── base
│   └── secret.yaml
├── database
│   ├── migrate-jobs.yaml
│   ├── orchestration-db.yaml
│   ├── parking-command-db.yaml
│   ├── parking-query-db.yaml
│   ├── pv.yaml
│   ├── vehicle-db.yaml
│   └── zone-db.yaml
├── frontend
│   └── deployment.yaml
├── parking-bot
│   └── parking-bot-deployment.yaml
└── README.md
├── docs
│   ├── build_Images.md
│   ├── deploy.md
│   ├── setup_Parking-Bot.md
│   └── test_Parking-Bot.md
```
