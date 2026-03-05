package com.mobs1.autoE.domain.zone.controller;

import com.mobs1.autoE.domain.zone.dto.CurrentParkingLocationResponse;
import com.mobs1.autoE.domain.zone.dto.TypeAvailabilityResponse;
import com.mobs1.autoE.domain.zone.dto.ZoneAvailabilityResponse;
import com.mobs1.autoE.domain.zone.service.ZoneAvailabilityService;
import com.mobs1.autoE.domain.zone.validator.VehicleNumberValidator;
import com.mobs1.autoE.global.apiResponse.code.SuccessCode;
import com.mobs1.autoE.global.apiResponse.response.ApiResponse;
import com.mobs1.autoE.global.enums.SlotCategory;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/zones")
public class ZoneAvailabilityController {

    private final ZoneAvailabilityService zoneAvailabilityService;

    public ZoneAvailabilityController(ZoneAvailabilityService zoneAvailabilityService) {
        this.zoneAvailabilityService = zoneAvailabilityService;
    }

    // 전체 여석 조회
    @GetMapping("/availability")
    public ResponseEntity<ApiResponse<List<ZoneAvailabilityResponse>>> getAllZonesAvailability() {
        return ok(zoneAvailabilityService.getAllZonesAvailability());
    }

    // 전체 존 타입 별 여석 수 반환
    @Operation(summary = "전체 Zone 타입 별 여석 수 반환", description = "전체 주차 가용 대수를 기준으로, 특정 타입 별로 주차 가능한 수를 반환합니다.")
    @GetMapping("/availability/type/{type}/count")
    public ResponseEntity<ApiResponse<Long>> getTotalAvailableByType(@PathVariable SlotCategory type) {
        return ok(zoneAvailabilityService.getTotalAvailableByType(type));
    }

    // A존 전체 여석 정보 반환
    @GetMapping("/{zoneId}/availability")
    public ResponseEntity<ApiResponse<ZoneAvailabilityResponse>> getZoneAvailability(@PathVariable Integer zoneId) {
        return ok(zoneAvailabilityService.getZoneAvailability(zoneId));
    }

    // A존 전체 여석 수 반환
    @Operation(summary = "특정 Zone 타입 별 여석 수 반환", description = "특정 Zone의 주차 가용 대수를 반환합니다.")
    @GetMapping("/{zoneId}/availability/count")
    public ResponseEntity<ApiResponse<Integer>> getZoneAvailableCount(@PathVariable Integer zoneId) {
        return ok(zoneAvailabilityService.getZoneAvailableCount(zoneId));
    }

    // A존 일반 타입 여석 정보 반환
    @GetMapping("/{zoneId}/availability/general")
    public ResponseEntity<ApiResponse<TypeAvailabilityResponse>> getGeneralAvailability(@PathVariable Integer zoneId) {
        return ok(zoneAvailabilityService.getZoneTypeAvailability(zoneId, SlotCategory.GENERAL));
    }

    // A존 일반 타입 여석 수 반환
    @GetMapping("/{zoneId}/availability/general/count")
    @Operation(summary = "특정 Zone 일반 타입 별 여석 수 반환", description = "특정 Zone의 주차 가용 대수를 기준으로, 특정 Zone에서 일반 타입이 주차 가능한 수를 반환합니다.")
    public ResponseEntity<ApiResponse<Integer>> getGeneralAvailableCount(@PathVariable Integer zoneId) {
        return ok(zoneAvailabilityService.getZoneTypeAvailableCount(zoneId, SlotCategory.GENERAL));
    }

    // A존 EV 타입 여석 정보 반환
    @GetMapping("/{zoneId}/availability/ev")
    public ResponseEntity<ApiResponse<TypeAvailabilityResponse>> getEvAvailability(@PathVariable Integer zoneId) {
        return ok(zoneAvailabilityService.getZoneTypeAvailability(zoneId, SlotCategory.EV));
    }

    // A존 EV 타입 여석 수 반환
    @GetMapping("/{zoneId}/availability/ev/count")
    @Operation(summary = "특정 Zone EV 타입 별 여석 수 반환", description = "특정 Zone의 주차 가용 대수를 기준으로, 특정 Zone에서 EV 타입이 주차 가능한 수를 반환합니다.")
    public ResponseEntity<ApiResponse<Integer>> getEvAvailableCount(@PathVariable Integer zoneId) {
        return ok(zoneAvailabilityService.getZoneTypeAvailableCount(zoneId, SlotCategory.EV));
    }

    // A존 Disabled 타입 여석 정보 반환
    @GetMapping("/{zoneId}/availability/disabled")
    public ResponseEntity<ApiResponse<TypeAvailabilityResponse>> getDisabledAvailability(@PathVariable Integer zoneId) {
        return ok(zoneAvailabilityService.getZoneTypeAvailability(zoneId, SlotCategory.DISABLED));
    }

    // A존 Disabled 타입 여석 수 반환
    @GetMapping("/{zoneId}/availability/disabled/count")
    @Operation(summary = "특정 Zone Disabled 타입 별 여석 수 반환", description = "특정 Zone의 주차 가용 대수를 기준으로, 특정 Zone에서 Disabled 타입이 주차 가능한 수를 반환합니다.")
    public ResponseEntity<ApiResponse<Integer>> getDisabledAvailableCount(@PathVariable Integer zoneId) {
        return ok(zoneAvailabilityService.getZoneTypeAvailableCount(zoneId, SlotCategory.DISABLED));
    }

    // 차량 번호 기준으로 주차한 지역 반환
    @GetMapping("/vehicles/{vehicleNum}/current-parking")
    @Operation(summary = "차량번호로 주차장 위치 반환", description = "입력한 차량 번호를 기준으로, 주차한 차량의 Zone_id와 slot_name을 반환합니다.")
    public ResponseEntity<ApiResponse<CurrentParkingLocationResponse>> getCurrentParkingByVehicleNum(@PathVariable String vehicleNum) {
        String normalizedVehicleNum = VehicleNumberValidator.normalizeAndValidate(vehicleNum);
        return ok(zoneAvailabilityService.getCurrentParkingLocation(normalizedVehicleNum));
    }

    private <T> ResponseEntity<ApiResponse<T>> ok(T data) {
        return ResponseEntity.ok(ApiResponse.success(SuccessCode.SUCCESS_READ, data));
    }
}
