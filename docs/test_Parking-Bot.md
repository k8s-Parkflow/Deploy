### 봇 로그 확인 (실시간)
```bash
kubectl logs -f deployment/parking-bot
```
<img width="1059" height="253" alt="image" src="https://github.com/user-attachments/assets/7803b3db-82a6-46a1-ba1e-b502bb9fcbba" />

### 🕵️ 실시간 모니터링 및 데이터 조회 가이드

### 1. [현재 주차 상황] 실시간 상세 현황 조회
```bash
kubectl exec -it deployment/parking-command-grpc -- /opt/venv/bin/python manage.py shell -c "
from parking_command_service.domains.parking_record.domain import ParkingHistory, ParkingSlot
from django.utils import timezone

# 1. 현재 주차 중인 모든 기록 로드
active_history = ParkingHistory.objects.filter(exit_at__isnull=True).order_by('slot_id')
history_map = {}
for h in active_history:
    history_map.setdefault(h.zone_id, []).append(h)

# 2. 모든 슬롯 정보를 한 번에 가져와서 캐싱
all_slots = {s.slot_id: s.slot_code for s in ParkingSlot.objects.all()}

print('\n' + '═'*90)
print(f' 🏎️  [실시간 상세 현황] 총 {active_history.count():4d} / 1,000 대 주차 중')
print('═'*90)

# 3. 1~10번 Zone을 순차적으로 출력
for z_id in range(1, 11):
    zone_cars = history_map.get(z_id, [])
    print(f'\n [ 🏢 ZONE {z_id:2d} ] ' + '─'*73)
    if not zone_cars:
        print('    ( ℹ️ 이 구역은 현재 비어 있습니다 )')
        continue
    for h in zone_cars:
        slot_code = all_slots.get(h.slot_id, f'ID:{h.slot_id:03d}')
        entry_time = h.entry_at.strftime('%Y-%m-%d %H:%M:%S')
        print(f'    🚗 {slot_code:6s} (ID:{h.slot_id:4d}) | 차량: {h.vehicle_num:10s} | 입차: {entry_time}')
print('\n' + '═'*90 + '\n')
"
```
<img width="1093" height="451" alt="image" src="https://github.com/user-attachments/assets/ddc6046d-986e-47ec-91e6-0228510f043e" />

### 2. [현재 등록 차량] 전체 리스트 조회
```bash
kubectl exec -it deployment/vehicle-grpc -- /opt/venv/bin/python manage.py shell -c "
from vehicle_service.models import Vehicle
all_vehicles = Vehicle.objects.all()
print(f'\n--- [ 등록된 차량 전체 리스트 ({all_vehicles.count()}대) ] ---')
for v in all_vehicles:
    print(f'🚗 번호판: {v.vehicle_num} | 타입: {v.vehicle_type}')
"
```
<img width="585" height="337" alt="image" src="https://github.com/user-attachments/assets/e058a265-dbb1-4051-8736-3a39889b207e" />

### 3. [구역별 점유 상황] 시각화 요약 조회
```bash
kubectl exec -it deployment/parking-command-grpc -- /opt/venv/bin/python manage.py shell -c "
from parking_command_service.domains.parking_record.domain import ParkingHistory, ParkingSlot
# 1. 데이터 로드
active_history = ParkingHistory.objects.filter(exit_at__isnull=True)
all_slots = ParkingSlot.objects.all()
print('\n' + '═'*85)
print(f' 🏎️  [대형 주차장 실시간 점유율 요약 - 1,000 Slots]')
print('─'*85)
total_parked = 0
total_slots = 0
# 2. 10개 Zone 순회 (1~10)
for zone_id in range(1, 11):
    z_total = all_slots.filter(zone_id=zone_id).count()
    z_parked = active_history.filter(zone_id=zone_id).count()
    total_parked += z_parked
    total_slots += z_total
    percent = (z_parked / z_total * 100) if z_total > 0 else 0
    bar_count = int(percent // 5)
    bar = '■' * bar_count + '□' * (20 - bar_count)
    print(f'  🏢 ZONE {zone_id:2d}: {z_parked:3d}/{z_total:3d} Slots | [{bar}] {percent:5.1f}%')

# 3. 전체 요약
total_percent = (total_parked / total_slots * 100) if total_slots > 0 else 0
print('─'*85)
print(f' 📈 TOTAL : {total_parked:4d}/{total_slots:4d} Occupied  |  Overall Density: {total_percent:5.1f}%')
print('═'*85 + '\n')
"
```
<img width="1065" height="379" alt="image" src="https://github.com/user-attachments/assets/f26541fd-2289-4959-95cf-79fbe19cfbfa" />
