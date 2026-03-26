📊 Initial Data Seeding Guide

### 1. [Zone 서비스] 구역 및 슬롯 1,000개 생성
```bash
kubectl exec -it deployment/zone-grpc -- /opt/venv/bin/python manage.py shell -c "
from zone_service.models import Zone, SlotType, ParkingSlot
import string
st_gen, _ = SlotType.objects.get_or_create(slot_type_id=1, defaults={'type_name': 'GENERAL'})
alphabets = string.ascii_uppercase[:10]
for z_idx in range(1, 11):
    zone_name, zone_code = f'ZONE-{z_idx}', alphabets[z_idx-1]
    z_obj, _ = Zone.objects.get_or_create(zone_id=z_idx, defaults={'zone_name': zone_name, 'is_active': True})
    start_id, end_id = (z_idx - 1) * 100 + 1, z_idx * 100 + 1
    for s_id in range(start_id, end_id):
        s_code = f'{zone_code}{s_id - (start_id-1):03d}'
        ParkingSlot.objects.get_or_create(slot_id=s_id, defaults={'zone': z_obj, 'slot_type': st_gen, 'slot_code': s_code, 'is_active': True})
    print(f' ✅ {zone_name} 완료')
"
```

### 2. [Command 서비스] 슬롯 데이터 동기화
```bash
kubectl exec -it deployment/parking-command-grpc -- /opt/venv/bin/python manage.py shell -c "
from parking_command_service.domains.parking_record.domain import ParkingSlot
import string
alphabets = string.ascii_uppercase[:10]
for z_idx in range(1, 11):
    zone_code = alphabets[z_idx-1]
    start_id, end_id = (z_idx - 1) * 100 + 1, z_idx * 100 + 1
    for s_id in range(start_id, end_id):
        s_code = f'{zone_code}{s_id - (start_id-1):03d}'
        ParkingSlot.objects.get_or_create(slot_id=s_id, defaults={'zone_id': z_idx, 'slot_code': s_code, 'is_active': True})
    print(f' ✅ ZONE-{z_idx} 동기화 완료')
"
```

### 3. [Vehicle 서비스] 차량 1,000대 등록 (100가0001~ 100가1000)
```bash
kubectl exec -it deployment/vehicle-grpc -- /opt/venv/bin/python manage.py shell -c "
from vehicle_service.models import Vehicle
for i in range(1, 1001):
    v_num = f'100가{1000 + i}'
    Vehicle.objects.get_or_create(vehicle_num=v_num, defaults={'vehicle_type': 'GENERAL'})
    if i % 200 == 0: print(f' ✅ {i}대 등록 중...')
print(' ✅ Vehicle 등록 최종 완료!')
"
```
