package com.mobs1.autoE.domain.zone.dto;

import com.mobs1.autoE.domain.zone.entity.ZoneAvailability;

public record ZoneAvailabilityResponse(
        Integer zoneId,
        String zoneName,
        int totalSlots,
        int occupiedSlots,
        int availableSlots,
        int generalAvailable,
        int evAvailable,
        int disabledAvailable
) {
    public static ZoneAvailabilityResponse from(ZoneAvailability availability) {
        return new ZoneAvailabilityResponse(
                availability.getId(),
                availability.getZone().getName(),
                availability.getTotalSlots(),
                availability.getOccupiedSlots(),
                availability.getAvailableSlots(),
                availability.getGeneralAvailable(),
                availability.getEvAvailable(),
                availability.getDisabledAvailable()
        );
    }
}
