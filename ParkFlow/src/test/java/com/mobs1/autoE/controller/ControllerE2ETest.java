package com.mobs1.autoE.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.mobs1.autoE.domain.zone.controller.ZoneAvailabilityController;
import com.mobs1.autoE.domain.zone.dto.TypeAvailabilityResponse;
import com.mobs1.autoE.domain.zone.dto.ZoneAvailabilityResponse;
import com.mobs1.autoE.domain.zone.service.ZoneAvailabilityService;
import com.mobs1.autoE.global.apiResponse.code.ErrorCode;
import com.mobs1.autoE.global.apiResponse.exception.BusinessException;
import com.mobs1.autoE.global.apiResponse.handler.GlobalExceptionHandler;
import com.mobs1.autoE.global.enums.SlotCategory;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * Standalone 컨트롤러 E2E(응답 포맷/경로 검증). 서비스는 Mockito mock으로 대체.
 */
class ControllerE2ETest {

    private MockMvc mockMvc;
    private ZoneAvailabilityService service;

    @BeforeEach
    void setUp() {
        service = Mockito.mock(ZoneAvailabilityService.class);
        ZoneAvailabilityController controller = new ZoneAvailabilityController(service);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Nested
    @DisplayName("ZoneAvailabilityController")
    class ZoneAvailabilityE2E {

        @Test
        @DisplayName("전체 존 여석 목록 조회")
        void getAllZonesAvailability() throws Exception {
            ZoneAvailabilityResponse a = new ZoneAvailabilityResponse(1, "A", 100, 52, 48, 12, 3, 3);
            ZoneAvailabilityResponse b = new ZoneAvailabilityResponse(2, "B", 100, 70, 30, 20, 5, 5);
            Mockito.when(service.getAllZonesAvailability()).thenReturn(List.of(a, b));

            mockMvc.perform(get("/zones/availability"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data[0].zoneName").value("A"))
                    .andExpect(jsonPath("$.data[1].availableSlots").value(30));
        }

        @Test
        @DisplayName("EV 타입 여석/카운트 조회")
        void getEvAvailability() throws Exception {
            Mockito.when(service.getZoneTypeAvailability(1, SlotCategory.EV))
                    .thenReturn(new TypeAvailabilityResponse(1, SlotCategory.EV, 10, 7, 3));
            Mockito.when(service.getZoneTypeAvailableCount(1, SlotCategory.EV)).thenReturn(3);

            mockMvc.perform(get("/zones/1/availability/ev"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.available").value(3))
                    .andExpect(jsonPath("$.data.category").value("EV"));

            mockMvc.perform(get("/zones/1/availability/ev/count"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").value(3));
        }

        @Test
        @DisplayName("Disabled 타입 여석/카운트 조회")
        void getDisabledAvailability() throws Exception {
            Mockito.when(service.getZoneTypeAvailability(1, SlotCategory.DISABLED))
                    .thenReturn(new TypeAvailabilityResponse(1, SlotCategory.DISABLED, 10, 7, 3));
            Mockito.when(service.getZoneTypeAvailableCount(1, SlotCategory.DISABLED)).thenReturn(3);

            mockMvc.perform(get("/zones/1/availability/disabled"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.available").value(3))
                    .andExpect(jsonPath("$.data.category").value("DISABLED"));

            mockMvc.perform(get("/zones/1/availability/disabled/count"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").value(3));
        }

        @Test
        @DisplayName("존이 없으면 404(E100)")
        void zoneNotFound() throws Exception {
            Mockito.when(service.getZoneAvailability(99))
                    .thenThrow(new BusinessException(ErrorCode.ZONE_NOT_FOUND));

            mockMvc.perform(get("/zones/99/availability"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.code").value("E100"));
        }
    }
}
