package com.mobs1.autoE.domain.park.repository;

import com.mobs1.autoE.domain.park.SlotOccupancy;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SlotOccupancyRepository extends JpaRepository<SlotOccupancy, Integer> {

    Optional<SlotOccupancy> findFirstByVehicleVehicleNumAndOccupiedTrueOrderByOccupiedSinceDesc(String vehicleNum);
}
