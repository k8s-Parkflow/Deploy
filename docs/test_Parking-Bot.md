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

# 1. 현재 주차 중인 모든 기록 로드
active_history = ParkingHistory.objects.filter(exit_at__isnull=True).order_by('slot_id')
history_map = {}
for h in active_history:
    history_map.setdefault(h.zone_id, []).append(h)

# 2. 모든 슬롯 정보를 캐싱
all_slots = {s.slot_id: s.slot_code for s in ParkingSlot.objects.all()}

print('\n' + '═'*95)
print(f' 🏎️  [실시간 상세 현황] 총 {active_history.count():4d} / 1,000 대 주차 중')
print('═'*95)

# 3. Zone별 출력
for z_id in range(1, 11):
    zone_cars = history_map.get(z_id, [])
    print(f'\n [ 🏢 ZONE {z_id:2d} ] ' + '─'*78)
    if not zone_cars:
        print('    ( ℹ️ 이 구역은 현재 비어 있습니다 )')
        continue
    for h in zone_cars:
        slot_code = all_slots.get(h.slot_id, f'ID:{h.slot_id:03d}')
        entry_time = h.entry_at.strftime('%Y-%m-%d %H:%M:%S')
        
        # 차량 번호 끝자리를 이용한 타입 판별 (7:2:1 규칙)
        try:
            last_digit = int(h.vehicle_num[-1])
            if 1 <= last_digit <= 7:
                v_type = 'GENERAL'
            elif last_digit in [8, 9]:
                v_type = 'EV'
            else:
                v_type = 'DISABLED'
        except:
            v_type = 'UNKNOWN'
            
        print(f'    🚗 {slot_code:6s} (ID:{h.slot_id:4d}) | {v_type:8s} | 차량: {h.vehicle_num:10s} | 입차: {entry_time}')
print('\n' + '═'*95 + '\n')
"
```
<img width="1147" height="621" alt="image" src="https://github.com/user-attachments/assets/6aea1dba-1ddf-455c-abf3-02386620b79f" />


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
<img width="592" height="497" alt="image" src="https://github.com/user-attachments/assets/3d07b0c5-8362-4d46-bfb0-e5fe1764fa32" />


### 3. [구역별 점유 상황] 시각화 요약 조회
```bash
kubectl exec -it deployment/parking-command-grpc -- /opt/venv/bin/python manage.py shell -c "
from parking_command_service.domains.parking_record.domain import ParkingHistory, ParkingSlot

# 1. 데이터 로드
active_history = ParkingHistory.objects.filter(exit_at__isnull=True)
all_slots = ParkingSlot.objects.all()

print('\n' + '═'*100)
print(f' 🏎️  [대형 주차장 실시간 점유율 및 타입별 현황 - 1,000 Slots]')
print('─'*100)

total_gen, total_ev, total_dis = 0, 0, 0

# 2. 10개 Zone 순회 (1~10)
for z_id in range(1, 11):
    z_slots = all_slots.filter(zone_id=z_id).count()
    z_cars = active_history.filter(zone_id=z_id)
    z_parked = z_cars.count()
    
    # 타입별 계산 (차량번호 끝자리 기준)
    z_gen = sum(1 for c in z_cars if 1 <= int(c.vehicle_num[-1]) <= 7)
    z_ev  = sum(1 for c in z_cars if int(c.vehicle_num[-1]) in [8, 9])
    z_dis = sum(1 for c in z_cars if int(c.vehicle_num[-1]) == 0)
    
    # 전체 합계용 저장
    total_gen += z_gen
    total_ev  += z_ev
    total_dis += z_dis
    
    # 시각화 바 생성 (5%당 한 칸)
    percent = (z_parked / z_slots * 100) if z_slots > 0 else 0
    bar = '■' * int(percent // 5) + '□' * (20 - int(percent // 5))
    
    # 출력 포맷팅
    print(f' 🏢 ZONE {z_id:2d}: {z_parked:3d}/{z_slots:3d} | [{bar}] {percent:4.1f}% | (일반:{z_gen:2d} 전기:{z_ev:d} 장애:{z_dis:d})')

# 3. 전체 요약
total_p = total_gen + total_ev + total_dis
total_s = all_slots.count()
total_per = (total_p / total_s * 100) if total_s > 0 else 0

print('─'*100)
print(f' 📈 TOTAL : {total_p:4d}/{total_s:4d} Occupied | Overall Density: {total_per:5.1f}%')
print(f' 📊 TYPE  : GENERAL: {total_gen:3d} | EV: {total_ev:2d} | DISABLED: {total_dis:2d}')
print('═'*100 + '\n')
"
```
<img width="1246" height="409" alt="image" src="https://github.com/user-attachments/assets/98ffab20-0193-41fa-80bb-f07504d617c9" />

