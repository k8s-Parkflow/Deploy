package com.mobs1.autoE.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mobs1.autoE.domain.park.SlotType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SlotTypeTest {

    @Test
    @DisplayName("슬롯 타입 이름을 변경할 수 있다")
    void changeType() {
        SlotType type = new SlotType("OLD");

        type.changeType("NEW");

        assertThat(type.getType()).isEqualTo("NEW");
    }
}
