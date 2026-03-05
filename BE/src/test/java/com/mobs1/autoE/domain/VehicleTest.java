package com.mobs1.autoE.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.mobs1.autoE.domain.zone.entity.Vehicle;
import com.mobs1.autoE.global.enums.SlotCategory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class VehicleTest {

    @Test
    @DisplayName("차량 타입 코드를 변경할 수 있다")
    void changeVehicleType() {
        Vehicle vehicle = new Vehicle("56다7890", SlotCategory.GENERAL);

        vehicle.changeType(SlotCategory.EV);

        assertThat(vehicle.getVehicleTypeCode()).isEqualTo(SlotCategory.EV);
    }

    @Test
    @DisplayName("차량 타입을 null로 변경하려 하면 예외를 던진다")
    void changeVehicleTypeToNullThrows() {
        Vehicle vehicle = new Vehicle("78라1234", SlotCategory.GENERAL);

        assertThatThrownBy(() -> vehicle.changeType(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("null이 될 수 없습니다");
    }
}
