package com.mobs1.autoE.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.mobs1.autoE.domain.park.repository.SlotOccupancyRepository;
import com.mobs1.autoE.domain.zone.dto.TypeAvailabilityResponse;
import com.mobs1.autoE.domain.zone.service.ZoneAvailabilityService;
import com.mobs1.autoE.domain.zone.dto.ZoneAvailabilityResponse;
import com.mobs1.autoE.domain.zone.entity.Zone;
import com.mobs1.autoE.domain.zone.entity.ZoneAvailability;
import com.mobs1.autoE.global.apiResponse.exception.BusinessException;
import com.mobs1.autoE.global.enums.SlotCategory;
import com.mobs1.autoE.domain.zone.repository.ZoneAvailabilityRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

/**
 * TDD: 서비스 요구사항을 먼저 테스트로 명세
 * Mockito로 Mock 주입해 순수 서비스 로직만 검증
 */

class ZoneAvailabilityServiceTddTest {

    private ZoneAvailabilityRepository repository;
    private SlotOccupancyRepository slotOccupancyRepository;
    private ZoneAvailabilityService service;

    private ZoneAvailability zoneA;
    private ZoneAvailability zoneB;
    private ZoneAvailability zoneC;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(ZoneAvailabilityRepository.class);
        slotOccupancyRepository = Mockito.mock(SlotOccupancyRepository.class);
        service = new ZoneAvailabilityService(repository, slotOccupancyRepository);

        Zone a = new Zone("A");
        Zone b = new Zone("B");
        Zone c = new Zone("C");
        LocalDateTime now = LocalDateTime.now();

        zoneA = new ZoneAvailability(a,
                100, 52, 48,   // total, occupied, available
                80, 68, 12,    // general
                10, 7, 3,      // ev
                10, 7, 3, now);// disabled
        zoneB = new ZoneAvailability(b,
                100, 70, 30,
                80, 60, 20,
                10, 5, 5,
                10, 5, 5, now);
        zoneC = new ZoneAvailability(c,
                100, 85, 15,
                80, 72, 8,
                10, 7, 3,
                10, 6, 4, now);

        // 기본 조회 스텁
        when(repository.findByZoneId(1)).thenReturn(Optional.of(zoneA));
        when(repository.findAvailableSlotsByZoneId(1)).thenReturn(Optional.of(zoneA.getAvailableSlots()));
        when(repository.findGeneralAvailableByZoneId(1)).thenReturn(Optional.of(zoneA.getGeneralAvailable()));
        when(repository.findEvAvailableByZoneId(1)).thenReturn(Optional.of(zoneA.getEvAvailable()));
        when(repository.findDisabledAvailableByZoneId(1)).thenReturn(Optional.of(zoneA.getDisabledAvailable()));
    }

    @Test
    @DisplayName("전체 존 여석 리스트를 반환")
    void listAllZones() {
        when(repository.findAll()).thenReturn(List.of(zoneA, zoneB, zoneC));

        List<ZoneAvailabilityResponse> result = service.getAllZonesAvailability();

        assertThat(result).hasSize(3);
        int sumAvailable = result.stream().mapToInt(ZoneAvailabilityResponse::availableSlots).sum();
        int sumGeneral = result.stream().mapToInt(ZoneAvailabilityResponse::generalAvailable).sum();
        int sumEv = result.stream().mapToInt(ZoneAvailabilityResponse::evAvailable).sum();
        int sumDisabled = result.stream().mapToInt(ZoneAvailabilityResponse::disabledAvailable).sum();

        assertThat(sumAvailable).isEqualTo(48 + 30 + 15);
        assertThat(sumGeneral).isEqualTo(12 + 20 + 8);
        assertThat(sumEv).isEqualTo(3 + 5 + 3);
        assertThat(sumDisabled).isEqualTo(3 + 5 + 4);
    }

    @Test
    @DisplayName("전체 존 여석 수 합계를 반환")
    void getAllZoneAvailableCount() {
        when(repository.findAll()).thenReturn(List.of(zoneA, zoneB, zoneC));

        List<ZoneAvailabilityResponse> result = service.getAllZonesAvailability();

        int sumAvailable = result.stream().mapToInt(ZoneAvailabilityResponse::availableSlots).sum();

        assertThat(sumAvailable).isEqualTo(48 + 30 + 15);
    }

    @Test
    @DisplayName("특정 존 전체 여석을 조회")
    void getZoneTotalAvailability() {
        when(repository.findByZoneId(1)).thenReturn(Optional.of(zoneA));

        ZoneAvailabilityResponse view = service.getZoneAvailability(1);

        assertThat(view.availableSlots()).isEqualTo(48);
        assertThat(view.zoneName()).isEqualTo("A");
        assertThat(view.totalSlots()).isEqualTo(100);
        assertThat(view.occupiedSlots()).isEqualTo(52);
    }

    @Test
    @DisplayName("특정 존 전체 여석 수만 반환")
    void getZoneAvailableCount() {
        when(repository.findByZoneId(1)).thenReturn(Optional.of(zoneA));

        int available = service.getZoneAvailableCount(1);

        assertThat(available).isEqualTo(48);
    }

    @Test
    @DisplayName("General 타입 여석을 조회")
    void getGeneralAvailability() {
        when(repository.findByZoneId(1)).thenReturn(Optional.of(zoneA));

        TypeAvailabilityResponse view = service.getZoneTypeAvailability(1, SlotCategory.GENERAL);

        assertThat(view.available()).isEqualTo(12);
        assertThat(view.total()).isEqualTo(80);
        assertThat(view.occupied()).isEqualTo(68);
    }

    @Test
    @DisplayName("General 타입 여석 num 반환")
    void getGeneralAvailableCount() {
        int available = service.getZoneTypeAvailableCount(1, SlotCategory.GENERAL);

        assertThat(available).isEqualTo(12);
    }

    @Test
    @DisplayName("EV 타입 여석을 조회")
    void getEvAvailability() {
        when(repository.findByZoneId(1)).thenReturn(Optional.of(zoneA));

        TypeAvailabilityResponse view = service.getZoneTypeAvailability(1, SlotCategory.EV);

        assertThat(view.available()).isEqualTo(3);
        assertThat(view.total()).isEqualTo(10);
        assertThat(view.occupied()).isEqualTo(7);
    }

    @Test
    @DisplayName("EV 타입 여석 num 반환")
    void getEvAvailableCount() {
        int available = service.getZoneTypeAvailableCount(1, SlotCategory.EV);

        assertThat(available).isEqualTo(3);
    }

    @Test
    @DisplayName("Disabled 타입 여석을 조회")
    void getDisabledAvailability() {
        when(repository.findByZoneId(1)).thenReturn(Optional.of(zoneA));

        TypeAvailabilityResponse view = service.getZoneTypeAvailability(1, SlotCategory.DISABLED);

        assertThat(view.available()).isEqualTo(3);
        assertThat(view.total()).isEqualTo(10);
        assertThat(view.occupied()).isEqualTo(7);
    }

    @Test
    @DisplayName("Disabled 타입 여석 num 반환")
    void getDisableAvailableCount() {
        int available = service.getZoneTypeAvailableCount(1, SlotCategory.DISABLED);

        assertThat(available).isEqualTo(3);
    }

    @Test
    @DisplayName("존이 없으면 404 예외 처리")
    void zoneNotFound() {
        when(repository.findByZoneId(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getZoneAvailability(99))
                .isInstanceOf(BusinessException.class);
    }
}
