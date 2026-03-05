package com.mobs1.autoE.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;

import com.mobs1.autoE.domain.park.ParkingHistory;
import com.mobs1.autoE.domain.park.ParkingSlot;
import com.mobs1.autoE.domain.park.SlotOccupancy;
import com.mobs1.autoE.domain.park.SlotType;
import com.mobs1.autoE.domain.zone.entity.Vehicle;
import com.mobs1.autoE.domain.zone.entity.Zone;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SlotOccupancyTest {

    private final Zone zone = new Zone("B");
    private final SlotType slotType = new SlotType("GENERAL");
    private final ParkingSlot slot = new ParkingSlot(zone, slotType, "B1");
    private final Vehicle vehicle = new Vehicle("12가3456", null);

    @Test
    @DisplayName("빈 슬롯을 점유하면 차량, 세션, 시간 정보가 설정된다")
    void occupyEmptySlot() {
        SlotOccupancy occupancy = new SlotOccupancy(slot, null, null, false, null, null);
        LocalDateTime now = LocalDateTime.now();
        ParkingHistory session = ParkingHistory.start(zone, slot, vehicle, now);

        occupancy.occupy(vehicle, session, now);

        assertThat(occupancy.isOccupied()).isTrue();
        assertThat(occupancy.getVehicle()).isEqualTo(vehicle);
        assertThat(occupancy.getCurrentSession()).isEqualTo(session);
        assertThat(occupancy.getOccupiedSince()).isEqualTo(now);
        assertThat(occupancy.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("이미 점유된 슬롯을 다시 점유하려 하면 예외를 던진다")
    void occupyAlreadyOccupiedThrows() {
        SlotOccupancy occupancy = new SlotOccupancy(slot, null, null, false, null, null);
        LocalDateTime now = LocalDateTime.now();
        ParkingHistory session = ParkingHistory.start(zone, slot, vehicle, now);
        occupancy.occupy(vehicle, session, now);

        assertThatThrownBy(() -> occupancy.occupy(vehicle, session, now.plusMinutes(1)))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("이미 점유된 슬롯입니다.");
    }

    @Test
    @DisplayName("점유된 슬롯을 해제하면 상태와 관련 참조를 초기화한다")
    void releaseOccupiedSlot() {
        SlotOccupancy occupancy = new SlotOccupancy(slot, null, null, false, null, null);
        LocalDateTime now = LocalDateTime.now();
        ParkingHistory session = ParkingHistory.start(zone, slot, vehicle, now);
        occupancy.occupy(vehicle, session, now);

        LocalDateTime releaseTime = now.plusMinutes(5);
        occupancy.release(releaseTime);

        assertThat(occupancy.isOccupied()).isFalse();
        assertThat(occupancy.getVehicle()).isNull();
        assertThat(occupancy.getCurrentSession()).isNull();
        assertThat(occupancy.getOccupiedSince()).isNull();
        assertThat(occupancy.getUpdatedAt()).isEqualTo(releaseTime);
    }

    @Test
    @DisplayName("비어있는 슬롯을 해제해도 상태 변화 없이 안전하게 반환한다")
    void releaseEmptySlotIsNoop() {
        SlotOccupancy occupancy = new SlotOccupancy(slot, null, null, false, null, null);
        LocalDateTime now = LocalDateTime.now();

        occupancy.release(now);

        assertThat(occupancy.isOccupied()).isFalse();
        assertThat(occupancy.getVehicle()).isNull();
        assertThat(occupancy.getCurrentSession()).isNull();
        assertThat(occupancy.getOccupiedSince()).isNull();
        assertThat(occupancy.getUpdatedAt()).isNull();
    }

    @Test
    @DisplayName("현재 세션 보유 여부(hasCurrentSession)를 분기별로 확인한다")
    void hasCurrentSessionCheck() {
        SlotOccupancy occupancy = new SlotOccupancy(slot, null, null, false, null, null);
        assertThat(occupancy.hasCurrentSession()).isFalse();

        LocalDateTime now = LocalDateTime.now();
        ParkingHistory session = ParkingHistory.start(zone, slot, vehicle, now);
        occupancy.occupy(vehicle, session, now);

        assertThat(occupancy.hasCurrentSession()).isTrue();
    }

    @Test
    @DisplayName("기본 생성자는 비점유 상태와 null 참조를 초기값으로 가진다")
    void defaultConstructorInitialState() {
        SlotOccupancy occupancy = new SlotOccupancy(slot, null, null, false, null, null);

        assertThat(occupancy.isOccupied()).isFalse();
        assertThat(occupancy.getVehicle()).isNull();
        assertThat(occupancy.getCurrentSession()).isNull();
        assertThat(occupancy.getOccupiedSince()).isNull();
        assertThat(occupancy.getUpdatedAt()).isNull();
    }

    @Test
    @DisplayName("모든 필드를 받는 생성자는 전달된 값으로 상태를 설정한다")
    void parameterizedConstructorSetsState() {
        LocalDateTime now = LocalDateTime.now();
        ParkingHistory session = ParkingHistory.start(zone, slot, vehicle, now.minusMinutes(5));
        SlotOccupancy occupancy = new SlotOccupancy(slot, session, vehicle, true, now.minusMinutes(5), now);

        assertThat(occupancy.isOccupied()).isTrue();
        assertThat(occupancy.getVehicle()).isEqualTo(vehicle);
        assertThat(occupancy.getCurrentSession()).isEqualTo(session);
        assertThat(occupancy.getOccupiedSince()).isEqualTo(now.minusMinutes(5));
        assertThat(occupancy.getUpdatedAt()).isEqualTo(now);
    }
}
