package com.mobs1.autoE.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import com.mobs1.autoE.domain.zone.entity.Zone;
import com.mobs1.autoE.domain.zone.entity.ZoneAvailability;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ZoneAvailabilityTest {

    private final Zone zone = new Zone("D");

    @Test
    @DisplayName("refreshCounts로 모든 집계 값을 일괄 업데이트한다")
    void refreshCounts() {
        ZoneAvailability availability = new ZoneAvailability(zone,
                0, 0, 0,
                0, 0, 0,
                0, 0, 0,
                0, 0, 0,
                null);
        LocalDateTime now = LocalDateTime.now();

        availability.refreshCounts(
                10, 4, 6,
                6, 2, 4,
                2, 1, 1,
                2, 1, 1,
                now);

        assertThat(availability.getTotalSlots()).isEqualTo(10);
        assertThat(availability.getOccupiedSlots()).isEqualTo(4);
        assertThat(availability.getAvailableSlots()).isEqualTo(6);
        assertThat(availability.getGeneralTotal()).isEqualTo(6);
        assertThat(availability.getGeneralOccupied()).isEqualTo(2);
        assertThat(availability.getGeneralAvailable()).isEqualTo(4);
        assertThat(availability.getEvTotal()).isEqualTo(2);
        assertThat(availability.getEvOccupied()).isEqualTo(1);
        assertThat(availability.getEvAvailable()).isEqualTo(1);
        assertThat(availability.getDisabledTotal()).isEqualTo(2);
        assertThat(availability.getDisabledOccupied()).isEqualTo(1);
        assertThat(availability.getDisabledAvailable()).isEqualTo(1);
        assertThat(availability.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("생성자에 전달한 초기 값이 그대로 세팅된다")
    void constructorSetsInitialCounts() {
        LocalDateTime now = LocalDateTime.now();
        ZoneAvailability availability = new ZoneAvailability(zone,
                10, 3, 7,
                6, 1, 5,
                2, 1, 1,
                2, 1, 1,
                now);

        assertThat(availability.getZone()).isEqualTo(zone);
        assertThat(availability.getTotalSlots()).isEqualTo(10);
        assertThat(availability.getOccupiedSlots()).isEqualTo(3);
        assertThat(availability.getAvailableSlots()).isEqualTo(7);
        assertThat(availability.getGeneralTotal()).isEqualTo(6);
        assertThat(availability.getGeneralOccupied()).isEqualTo(1);
        assertThat(availability.getGeneralAvailable()).isEqualTo(5);
        assertThat(availability.getEvTotal()).isEqualTo(2);
        assertThat(availability.getEvOccupied()).isEqualTo(1);
        assertThat(availability.getEvAvailable()).isEqualTo(1);
        assertThat(availability.getDisabledTotal()).isEqualTo(2);
        assertThat(availability.getDisabledOccupied()).isEqualTo(1);
        assertThat(availability.getDisabledAvailable()).isEqualTo(1);
        assertThat(availability.getUpdatedAt()).isEqualTo(now);
    }
}
