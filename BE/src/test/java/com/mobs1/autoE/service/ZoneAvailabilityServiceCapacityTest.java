package com.mobs1.autoE.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.mobs1.autoE.domain.park.repository.SlotOccupancyRepository;
import com.mobs1.autoE.domain.zone.service.ZoneAvailabilityService;
import com.mobs1.autoE.global.enums.SlotCategory;
import com.mobs1.autoE.domain.zone.repository.ZoneAvailabilityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * 전체 존에서 타입별 가용 공간 합계를 계산하는 서비스 요구사항을 먼저 정의한다.
 */
class ZoneAvailabilityServiceCapacityTest {

    private ZoneAvailabilityRepository repository;
    private SlotOccupancyRepository slotOccupancyRepository;
    private ZoneAvailabilityService service;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(ZoneAvailabilityRepository.class);
        slotOccupancyRepository = Mockito.mock(SlotOccupancyRepository.class);
        service = new ZoneAvailabilityService(repository, slotOccupancyRepository);
    }

    @Test
    @DisplayName("전체 존에서 타입별 빈자리 를 반환한다")
    void getGlobalAvailableByType() {
        when(repository.sumGeneralAvailable()).thenReturn(60L);
        when(repository.sumEvAvailable()).thenReturn(13L);
        when(repository.sumDisabledAvailable()).thenReturn(-3L);

        assertThat(service.getTotalAvailableByType(SlotCategory.GENERAL)).isEqualTo(60);
        assertThat(service.getTotalAvailableByType(SlotCategory.EV)).isEqualTo(13);
        // 데이터 이상 여부 검증용
        assertThat(service.getTotalAvailableByType(SlotCategory.DISABLED)).isEqualTo(-3);
    }
}
