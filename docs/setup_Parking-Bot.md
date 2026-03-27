📊 Initial Data Seeding Guide

### 1. [Vehicle 서비스] 차량 1,000대 등록 (100가1001~ 100가2000), (일반:전기:장애인 => 7:2:1)
```bash
kubectl exec -it deployment/vehicle-grpc -- /opt/venv/bin/python manage.py shell -c "
from vehicle_service.models import Vehicle
for i in range(1001, 2001):
    v_num = f'100가{i}'
    remainder = i % 10
    
    # 규칙: 1~7(일반), 8~9(전기차), 0(장애인)
    if 1 <= remainder <= 7:
        v_type = 'GENERAL'
    elif remainder == 8 or remainder == 9:
        v_type = 'EV'
    else: # remainder == 0
        v_type = 'DISABLED'
    
    # 생성 또는 업데이트
    Vehicle.objects.update_or_create(
        vehicle_num=v_num, 
        defaults={'vehicle_type': v_type}
    )
    
    if i % 200 == 0: 
        print(f' ✅ .. {i}번 차량 등록 중.. ')

print('✅ 1001~2000 차량 등록 최종 완료 (7:2:1)!')
"
```

### 2. [Zone 서비스] 구역 및 슬롯 1,000개 생성
```bash
kubectl exec -it deployment/zone-grpc -- /opt/venv/bin/python manage.py shell -c "
from zone_service.models import Zone, SlotType, ParkingSlot
import string

# 1. 타입 생성 (ID 명시적 부여)
st_gen, _ = SlotType.objects.get_or_create(slot_type_id=1, defaults={'type_name': 'GENERAL'})
st_ev, _ = SlotType.objects.get_or_create(slot_type_id=2, defaults={'type_name': 'EV'})
st_dis, _ = SlotType.objects.get_or_create(slot_type_id=3, defaults={'type_name': 'DISABLED'})

alphabets = string.ascii_uppercase[:10]

print('🚀 주차 슬롯 마스터 데이터 생성을 시작합니다 (7:2:1 비율)...')

for z_idx in range(1, 11):
    zone_name, zone_code = f'ZONE-{z_idx}', alphabets[z_idx-1]
    z_obj, _ = Zone.objects.get_or_create(zone_id=z_idx, defaults={'zone_name': zone_name, 'is_active': True})
    
    start_id, end_id = (z_idx - 1) * 100 + 1, z_idx * 100 + 1
    
    for s_id in range(start_id, end_id):
        # 7:2:1 규칙 적용
        remainder = s_id % 10
        if 1 <= remainder <= 7:
            s_type = st_gen
        elif remainder == 8 or remainder == 9:
            s_type = st_ev
        else:
            s_type = st_dis
            
        s_code = f'{zone_code}{s_id - (start_id-1):03d}'
        
        # 생성 또는 업데이트
        obj, created = ParkingSlot.objects.update_or_create(
            slot_id=s_id, 
            defaults={'zone': z_obj, 'slot_type': s_type, 'slot_code': s_code, 'is_active': True}
        )
         	
    print(f' ✅ {zone_name} 등록 완료! (ID: {start_id}~{end_id-1})')

print('✨ 모든 데이터 등록이 완료되었습니다.')
"
```

### 3. [Command 서비스] 슬롯 데이터 동기화
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
