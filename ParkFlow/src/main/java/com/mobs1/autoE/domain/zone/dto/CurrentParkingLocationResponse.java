package com.mobs1.autoE.domain.zone.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CurrentParkingLocationResponse(
        @JsonProperty("zone_id") String zoneId,
        @JsonProperty("slot_name") String slotName
) {
}
