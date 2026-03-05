package com.mobs1.autoE.domain.park;

import com.mobs1.autoE.domain.zone.entity.Vehicle;
import com.mobs1.autoE.domain.zone.entity.Zone;
import com.mobs1.autoE.global.enums.ParkingStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "PARKING_history")
public class ParkingHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "zone_id", nullable = false)
    private Zone zone;

    @ManyToOne(optional = false)
    @JoinColumn(name = "slot_id", nullable = false)
    private ParkingSlot slot;

    @ManyToOne(optional = false)
    @JoinColumn(name = "vehicle_plate", referencedColumnName = "vehicle_num", nullable = false)
    private Vehicle vehicle;

    @Column(name = "entry_at", nullable = false)
    private LocalDateTime entryAt;

    @Column(name = "exit_at")
    private LocalDateTime exitAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ParkingStatus status;

    protected ParkingHistory() {
        // JPA default constructor
    }

    private ParkingHistory(Zone zone,
                           ParkingSlot slot,
                           Vehicle vehicle,
                           LocalDateTime entryAt,
                           LocalDateTime exitAt,
                           ParkingStatus status) {
        this.zone = zone;
        this.slot = slot;
        this.vehicle = vehicle;
        this.entryAt = entryAt;
        this.exitAt = exitAt;
        this.status = status;
    }

    public static ParkingHistory start(Zone zone, ParkingSlot slot, Vehicle vehicle, LocalDateTime entryAt) {
        return new ParkingHistory(zone, slot, vehicle, entryAt, null, ParkingStatus.PARKED);
    }

    public Zone getZone() {
        return zone;
    }

    public ParkingSlot getSlot() {
        return slot;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public LocalDateTime getEntryAt() {
        return entryAt;
    }

    public LocalDateTime getExitAt() {
        return exitAt;
    }

    public ParkingStatus getStatus() {
        return status;
    }

    public void exit(LocalDateTime exitAt) {
        //TODO : 추가 예외처리 개선 필요
        if (status == ParkingStatus.EXITED) {
            throw new IllegalStateException("이미 출차 처리된 이력입니다.");
        }
        if (exitAt == null || exitAt.isBefore(entryAt)) {
            throw new IllegalArgumentException("출차 시간은 입차 시간 이후여야 합니다.");
        }
        this.exitAt = exitAt;
        this.status = ParkingStatus.EXITED;
    }
}
