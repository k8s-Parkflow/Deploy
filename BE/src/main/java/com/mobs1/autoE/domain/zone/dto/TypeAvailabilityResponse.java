package com.mobs1.autoE.domain.zone.dto;

import com.mobs1.autoE.global.enums.SlotCategory;

public record TypeAvailabilityResponse(
        Integer zoneId,
        SlotCategory category,
        int total,
        int occupied,
        int available
) {
}
