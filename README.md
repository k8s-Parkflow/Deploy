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
