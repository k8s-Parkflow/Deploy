package com.mobs1.autoE.domain.zone.service;

import com.mobs1.autoE.domain.park.SlotOccupancy;
import com.mobs1.autoE.domain.park.repository.SlotOccupancyRepository;
import com.mobs1.autoE.domain.zone.dto.CurrentParkingLocationResponse;
import com.mobs1.autoE.domain.zone.dto.TypeAvailabilityResponse;
import com.mobs1.autoE.domain.zone.dto.ZoneAvailabilityResponse;
import com.mobs1.autoE.domain.zone.entity.ZoneAvailability;
import com.mobs1.autoE.global.apiResponse.code.ErrorCode;
import com.mobs1.autoE.global.apiResponse.exception.BusinessException;
import com.mobs1.autoE.global.enums.SlotCategory;
import com.mobs1.autoE.domain.zone.repository.ZoneAvailabilityRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ZoneAvailabilityService {

    private final ZoneAvailabilityRepository availabilityRepository;
    private final SlotOccupancyRepository slotOccupancyRepository;

    public ZoneAvailabilityService(ZoneAvailabilityRepository availabilityRepository,
                                   SlotOccupancyRepository slotOccupancyRepository) {
        this.availabilityRepository = availabilityRepository;
        this.slotOccupancyRepository = slotOccupancyRepository;
    }

    // 전체 여석 조회 메서드
    public List<ZoneAvailabilityResponse> getAllZonesAvailability() {
        return availabilityRepository.findAll()
                .stream()
                .map(ZoneAvailabilityResponse::from)
                .collect(Collectors.toList());
    }


    public ZoneAvailabilityResponse getZoneAvailability(Integer zoneId) {
        return ZoneAvailabilityResponse.from(findAvailabilityOrThrow(zoneId));
    }

    public TypeAvailabilityResponse getZoneTypeAvailability(Integer zoneId, SlotCategory category) {
        ZoneAvailability availability = findAvailabilityOrThrow(zoneId);
        return toTypeAvailabilityResponse(availability, category);
    }

    // A 존의 전체 여석 수 반환
    public int getZoneAvailableCount(Integer zoneId) {
        return availabilityRepository.findAvailableSlotsByZoneId(zoneId)
                .orElseThrow(this::zoneNotFound);
    }

    // A 존의 타입 별 여석 수 반환
    public int getZoneTypeAvailableCount(Integer zoneId, SlotCategory category) {
        return (switch (category) {
            case GENERAL -> availabilityRepository.findGeneralAvailableByZoneId(zoneId);
            case EV -> availabilityRepository.findEvAvailableByZoneId(zoneId);
            case DISABLED -> availabilityRepository.findDisabledAvailableByZoneId(zoneId);
            default -> throw new BusinessException(ErrorCode.SLOT_CATEGORY_NOT_SUPPORTED);
        }).orElseThrow(this::zoneNotFound);
    }

    // 타입 별 정보조회 메서드
    private TypeAvailabilityResponse toTypeAvailabilityResponse(ZoneAvailability availability, SlotCategory category) {
        if (category == null) {
            throw new BusinessException(ErrorCode.SLOT_CATEGORY_NOT_SUPPORTED);
        }

        return switch (category) {
            case GENERAL -> new TypeAvailabilityResponse(
                    availability.getId(),
                    category,
                    availability.getGeneralTotal(),
                    availability.getGeneralOccupied(),
                    availability.getGeneralAvailable());
            case EV -> new TypeAvailabilityResponse(
                    availability.getId(),
                    category,
                    availability.getEvTotal(),
                    availability.getEvOccupied(),
                    availability.getEvAvailable());
            case DISABLED -> new TypeAvailabilityResponse(
                    availability.getId(),
                    category,
                    availability.getDisabledTotal(),
                    availability.getDisabledOccupied(),
                    availability.getDisabledAvailable());
        };
    }

    private ZoneAvailability findAvailabilityOrThrow(Integer zoneId) {
        return availabilityRepository.findByZoneId(zoneId)
                .orElseThrow(this::zoneNotFound);
    }

    private BusinessException zoneNotFound() {
        return businessError(ErrorCode.ZONE_NOT_FOUND);
    }

    public Long getTotalAvailableByType(SlotCategory category) {
        return switch (category) {
            case GENERAL -> availabilityRepository.sumGeneralAvailable();
            case EV -> availabilityRepository.sumEvAvailable();
            case DISABLED -> availabilityRepository.sumDisabledAvailable();
        };
    }

    public CurrentParkingLocationResponse getCurrentParkingLocation(String vehicleNum) {
        SlotOccupancy occupancy = findCurrentOccupancy(vehicleNum);
        return toCurrentParkingLocationResponse(occupancy);
    }

    private SlotOccupancy findCurrentOccupancy(String vehicleNum) {
        return slotOccupancyRepository
                .findFirstByVehicleVehicleNumAndOccupiedTrueOrderByOccupiedSinceDesc(vehicleNum)
                .orElseThrow(this::currentParkingNotFound);
    }

    private CurrentParkingLocationResponse toCurrentParkingLocationResponse(SlotOccupancy occupancy) {
        var slot = occupancy.getSlot();
        return new CurrentParkingLocationResponse(
                String.valueOf(slot.getZone().getId()),
                slot.getSlotCode());
    }

    private BusinessException currentParkingNotFound() {
        return businessError(ErrorCode.CURRENT_PARKING_NOT_FOUND);
    }

    private BusinessException businessError(ErrorCode errorCode) {
        return new BusinessException(errorCode);
    }
}
