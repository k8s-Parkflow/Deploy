package com.mobs1.autoE.domain.zone.repository;

import com.mobs1.autoE.domain.zone.entity.ZoneAvailability;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ZoneAvailabilityRepository extends JpaRepository<ZoneAvailability, Integer> {

    Optional<ZoneAvailability> findByZoneId(Integer zoneId);

    @Query("select za.availableSlots from ZoneAvailability za where za.id = :zoneId")
    Optional<Integer> findAvailableSlotsByZoneId(@Param("zoneId") Integer zoneId);

    @Query("select za.generalAvailable from ZoneAvailability za where za.id = :zoneId")
    Optional<Integer> findGeneralAvailableByZoneId(@Param("zoneId") Integer zoneId);

    @Query("select za.evAvailable from ZoneAvailability za where za.id = :zoneId")
    Optional<Integer> findEvAvailableByZoneId(@Param("zoneId") Integer zoneId);

    @Query("select za.disabledAvailable from ZoneAvailability za where za.id = :zoneId")
    Optional<Integer> findDisabledAvailableByZoneId(@Param("zoneId") Integer zoneId);

    // 전체 존 타입별 가용 합계
    @Query("select coalesce(sum(za.generalAvailable),0L) from ZoneAvailability za")
    Long sumGeneralAvailable();

    @Query("select coalesce(sum(za.evAvailable),0L) from ZoneAvailability za")
    Long sumEvAvailable();

    @Query("select coalesce(sum(za.disabledAvailable),0L) from ZoneAvailability za")
    Long sumDisabledAvailable();
}
