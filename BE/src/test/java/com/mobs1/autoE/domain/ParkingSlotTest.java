package com.mobs1.autoE.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.mobs1.autoE.domain.park.ParkingSlot;
import com.mobs1.autoE.domain.park.SlotType;
import com.mobs1.autoE.domain.zone.entity.Zone;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ParkingSlotTest {

    private final Zone zone = new Zone("A");
    private final SlotType slotType = new SlotType("EV");

    @Test
    @DisplayName("신규 슬롯은 기본적으로 활성 상태다")
    void defaultActiveIsTrue() {
        ParkingSlot slot = new ParkingSlot(zone, slotType, "A1");

        assertThat(slot.isActive()).isTrue();
    }

    @Test
    @DisplayName("슬롯 활성/비활성 상태를 의도된 메서드로 전환한다")
    void activateAndDeactivate() {
        ParkingSlot slot = new ParkingSlot(zone, slotType, "A2");

        slot.deactivate();
        assertThat(slot.isActive()).isFalse();

        slot.activate();
        assertThat(slot.isActive()).isTrue();
    }

    @Test
    @DisplayName("이미 비활성 상태에서 다시 비활성화하면 예외를 던진다")
    void deactivateTwiceThrows() {
        ParkingSlot slot = new ParkingSlot(zone, slotType, "A3", false);

        assertThat(slot.isActive()).isFalse();
        assertThatThrownBy(slot::deactivate)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("이미 비활성화된 슬롯입니다.");
    }

    @Test
    @DisplayName("이미 활성 상태에서 다시 활성화하면 예외를 던진다")
    void activateTwiceThrows() {
        ParkingSlot slot = new ParkingSlot(zone, slotType, "A4");

        assertThat(slot.isActive()).isTrue();
        assertThatThrownBy(slot::activate)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("이미 활성화된 슬롯입니다.");
    }

    @Test
    @DisplayName("생성 시 active 플래그를 직접 지정할 수 있다")
    void constructWithActiveFlag() {
        ParkingSlot slot = new ParkingSlot(zone, slotType, "A5", false);

        assertThat(slot.getZone()).isEqualTo(zone);
        assertThat(slot.getSlotType()).isEqualTo(slotType);
        assertThat(slot.getSlotCode()).isEqualTo("A5");
        assertThat(slot.isActive()).isFalse();
    }
}
