package com.mobs1.autoE.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mobs1.autoE.domain.park.ParkingHistory;
import com.mobs1.autoE.domain.park.ParkingSlot;
import com.mobs1.autoE.domain.park.SlotType;
import com.mobs1.autoE.domain.zone.entity.Vehicle;
import com.mobs1.autoE.domain.zone.entity.Zone;
import com.mobs1.autoE.global.enums.ParkingStatus;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ParkingHistoryTest {

    private final Zone zone = new Zone("C");
    private final SlotType slotType = new SlotType("DISABLED");
    private final ParkingSlot slot = new ParkingSlot(zone, slotType, "C1");
    private final Vehicle vehicle = new Vehicle("34나5678", null);

    @Test
    @DisplayName("주차 시작 시 상태는 PARKED이고 exitAt은 비어있다")
    void startParking() {
        LocalDateTime entry = LocalDateTime.now();

        ParkingHistory history = ParkingHistory.start(zone, slot, vehicle, entry);

        assertThat(history.getStatus()).isEqualTo(ParkingStatus.PARKED);
        assertThat(history.getEntryAt()).isEqualTo(entry);
        assertThat(history.getExitAt()).isNull();
    }

    @Test
    @DisplayName("출차 처리 시 상태를 EXITED로 변경하고 exit 시간을 기록한다")
    void exitParking() {
        LocalDateTime entry = LocalDateTime.now();
        ParkingHistory history = ParkingHistory.start(zone, slot, vehicle, entry);
        LocalDateTime exit = entry.plusHours(1);

        history.exit(exit);

        assertThat(history.getStatus()).isEqualTo(ParkingStatus.EXITED);
        assertThat(history.getExitAt()).isEqualTo(exit);
    }
}
