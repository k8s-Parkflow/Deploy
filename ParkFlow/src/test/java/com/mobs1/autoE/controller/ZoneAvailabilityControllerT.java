package com.mobs1.autoE.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.mobs1.autoE.domain.zone.controller.ZoneAvailabilityController;
import com.mobs1.autoE.domain.zone.dto.CurrentParkingLocationResponse;
import com.mobs1.autoE.domain.zone.dto.TypeAvailabilityResponse;
import com.mobs1.autoE.domain.zone.dto.ZoneAvailabilityResponse;
import com.mobs1.autoE.domain.zone.service.ZoneAvailabilityService;
import com.mobs1.autoE.global.apiResponse.code.ErrorCode;
import com.mobs1.autoE.global.apiResponse.exception.BusinessException;
import com.mobs1.autoE.global.apiResponse.handler.GlobalExceptionHandler;
import com.mobs1.autoE.global.enums.SlotCategory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

class ZoneAvailabilityControllerT {

    private MockMvc mockMvc;
    private ZoneAvailabilityService service;

    private ZoneAvailabilityResponse zoneA;
    private TypeAvailabilityResponse general;

    @BeforeEach
    void setUp() {
        service = Mockito.mock(ZoneAvailabilityService.class);
        ZoneAvailabilityController controller = new ZoneAvailabilityController(service);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        zoneA = new ZoneAvailabilityResponse(1, "A", 100, 52, 48, 12, 3, 3);
        general = new TypeAvailabilityResponse(1, SlotCategory.GENERAL, 80, 68, 12);
    }

    @Test
    @DisplayName("전체 존 여석 목록을 반환한다")
    void getAllZonesAvailability() throws Exception {
        ZoneAvailabilityResponse b = new ZoneAvailabilityResponse(2, "B", 100, 70, 30, 20, 5, 5);
        when(service.getAllZonesAvailability()).thenReturn(List.of(zoneA, b));

        mockMvc.perform(get("/zones/availability"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].zoneName").value("A"))
                .andExpect(jsonPath("$.data[1].availableSlots").value(30));
    }

    @Test
    @DisplayName("존 전체 여석과 count를 반환한다")
    void getZoneAvailability() throws Exception {
        when(service.getZoneAvailability(1)).thenReturn(zoneA);
        when(service.getZoneAvailableCount(1)).thenReturn(48);

        mockMvc.perform(get("/zones/1/availability"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.availableSlots").value(48));

        mockMvc.perform(get("/zones/1/availability/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(48));
    }

    @Test
    @DisplayName("General 타입 여석과 count를 반환한다")
    void getGeneralAvailability() throws Exception {
        when(service.getZoneTypeAvailability(1, SlotCategory.GENERAL)).thenReturn(general);
        when(service.getZoneTypeAvailableCount(1, SlotCategory.GENERAL)).thenReturn(12);

        mockMvc.perform(get("/zones/1/availability/general"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.available").value(12))
                .andExpect(jsonPath("$.data.category").value("GENERAL"));

        mockMvc.perform(get("/zones/1/availability/general/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(12));
    }

    @Test
    @DisplayName("EV 타입 여석과 count를 반환한다")
    void getEvAvailability() throws Exception {
        TypeAvailabilityResponse ev = new TypeAvailabilityResponse(1, SlotCategory.EV, 10, 7, 3);
        when(service.getZoneTypeAvailability(1, SlotCategory.EV)).thenReturn(ev);
        when(service.getZoneTypeAvailableCount(1, SlotCategory.EV)).thenReturn(3);

        mockMvc.perform(get("/zones/1/availability/ev"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.available").value(3))
                .andExpect(jsonPath("$.data.category").value("EV"));

        mockMvc.perform(get("/zones/1/availability/ev/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(3));
    }

    @Test
    @DisplayName("Disabled 타입 여석과 count를 반환한다")
    void getDisabledAvailability() throws Exception {
        TypeAvailabilityResponse disabled = new TypeAvailabilityResponse(1, SlotCategory.DISABLED, 10, 7, 3);
        when(service.getZoneTypeAvailability(1, SlotCategory.DISABLED)).thenReturn(disabled);
        when(service.getZoneTypeAvailableCount(1, SlotCategory.DISABLED)).thenReturn(3);

        mockMvc.perform(get("/zones/1/availability/disabled"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.available").value(3))
                .andExpect(jsonPath("$.data.category").value("DISABLED"));

        mockMvc.perform(get("/zones/1/availability/disabled/count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(3));
    }

    @Test
    @DisplayName("존이 없으면 404 응답을 반환한다")
    void zoneNotFound() throws Exception {
        when(service.getZoneAvailability(99)).thenThrow(new BusinessException(ErrorCode.ZONE_NOT_FOUND));

        mockMvc.perform(get("/zones/99/availability"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("E100"));
    }

    @Test
    @DisplayName("지원하지 않는 타입이면 400 응답을 반환한다")
    void unsupportedCategory() throws Exception {
        when(service.getZoneTypeAvailability(1, SlotCategory.GENERAL))
                .thenThrow(new BusinessException(ErrorCode.SLOT_CATEGORY_NOT_SUPPORTED));

        mockMvc.perform(get("/zones/1/availability/general"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("E101"));
    }

    @Test
    @DisplayName("차량 번호로 현재 주차 위치(zone_id, slot_name)를 조회한다")
    void getCurrentParkingLocationByVehicleNumber() throws Exception {
        when(service.getCurrentParkingLocation("12가3456"))
                .thenReturn(new CurrentParkingLocationResponse("1", "A-12"));

        mockMvc.perform(get("/zones/vehicles/12가3456/current-parking"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.zone_id").value("1"))
                .andExpect(jsonPath("$.data.slot_name").value("A-12"));
    }

    @Test
    @DisplayName("현재 주차 정보가 없으면 404(E102)를 반환한다")
    void currentParkingNotFound() throws Exception {
        when(service.getCurrentParkingLocation("99다9999"))
                .thenThrow(new BusinessException(ErrorCode.CURRENT_PARKING_NOT_FOUND));

        mockMvc.perform(get("/zones/vehicles/99다9999/current-parking"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("E102"));
    }

    @Test
    @DisplayName("차량 번호 형식이 잘못되면 400(E000)을 반환한다")
    void invalidVehicleNumber() throws Exception {
        mockMvc.perform(get("/zones/vehicles/ABC123/current-parking"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("E000"));
    }

    @Test
    @DisplayName("구분 문자 제거 후에도 차량 번호가 유효하지 않으면 400(E000)을 반환한다")
    void invalidVehicleNumberAfterNormalization() throws Exception {
        mockMvc.perform(get("/zones/vehicles/12가-34/current-parking"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("E000"));
    }

    @Test
    @DisplayName("공백이 포함된 차량 번호는 정규화 후 현재 주차 위치를 반환한다")
    void getCurrentParkingLocationByVehicleNumberWithWhitespace() throws Exception {
        when(service.getCurrentParkingLocation("12가3456"))
                .thenReturn(new CurrentParkingLocationResponse("1", "A-12"));

        mockMvc.perform(get("/zones/vehicles/12가 3456/current-parking"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.zone_id").value("1"))
                .andExpect(jsonPath("$.data.slot_name").value("A-12"));
    }

    @Test
    @DisplayName("하이픈이 포함된 차량 번호는 정규화 후 현재 주차 위치를 반환한다")
    void getCurrentParkingLocationByVehicleNumberWithHyphen() throws Exception {
        when(service.getCurrentParkingLocation("12가3456"))
                .thenReturn(new CurrentParkingLocationResponse("1", "A-12"));

        mockMvc.perform(get("/zones/vehicles/12가-3456/current-parking"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.zone_id").value("1"))
                .andExpect(jsonPath("$.data.slot_name").value("A-12"));
    }
}
