package com.mobs1.autoE.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mobs1.autoE.domain.zone.entity.Zone;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ZoneTest {

    @Test
    @DisplayName("존 이름을 변경할 수 있다")
    void renameZone() {
        Zone zone = new Zone("Old");

        zone.rename("New");

        assertThat(zone.getName()).isEqualTo("New");
    }
}
