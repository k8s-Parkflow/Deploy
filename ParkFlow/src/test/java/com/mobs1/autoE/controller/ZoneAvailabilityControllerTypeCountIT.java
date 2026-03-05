package com.mobs1.autoE.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.mobs1.autoE.domain.zone.controller.ZoneAvailabilityController;
import com.mobs1.autoE.domain.zone.service.ZoneAvailabilityService;
import com.mobs1.autoE.global.apiResponse.handler.GlobalExceptionHandler;
import com.mobs1.autoE.global.enums.SlotCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * 전체 존 타입별 가용 슬롯 카운트 API 통합 테스트(standalone).
 */
class ZoneAvailabilityControllerTypeCountIT {

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

    @Test
    @DisplayName("전체 Zone에서 General 타입 기준으로 가용 슬롯 수를 반환")
    void getTotalAvailableByType() throws Exception {
        Mockito.when(service.getTotalAvailableByType(SlotCategory.GENERAL)).thenReturn(1L);

        mockMvc.perform(get("/zones/availability/type/GENERAL/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(1));
    }

}
