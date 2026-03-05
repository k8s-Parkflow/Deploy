package com.mobs1.autoE.domain.park;

import com.mobs1.autoE.domain.zone.entity.Vehicle;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "SLOT_OCCUPANCY")
public class SlotOccupancy {

    @Id
    @Column(name = "slot_id")
    private Integer id;

    @OneToOne(optional = false)
    @MapsId
    @JoinColumn(name = "slot_id")
    private ParkingSlot slot;

    @Column(name = "occupied", nullable = false)
    private boolean occupied = false;

    @OneToOne
    @JoinColumn(name = "current_session_id")
    private ParkingHistory currentSession;

    @ManyToOne
    @JoinColumn(name = "vehicle_plate", referencedColumnName = "vehicle_num")
    private Vehicle vehicle;

    @Column(name = "occupied_since")
    private LocalDateTime occupiedSince;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    protected SlotOccupancy() {
    }

    public SlotOccupancy(ParkingSlot slot,
                         ParkingHistory currentSession,
                         Vehicle vehicle,
                         boolean occupied,
                         LocalDateTime occupiedSince,
                         LocalDateTime updatedAt) {
        this.slot = slot;
        this.currentSession = currentSession;
        this.vehicle = vehicle;
        this.occupied = occupied;
        this.occupiedSince = occupiedSince;
        this.updatedAt = updatedAt;
    }

    // getter
    public Integer getId() {
        return id;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public ParkingSlot getSlot() {
        return slot;
    }

    public ParkingHistory getCurrentSession() {
        return currentSession;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public LocalDateTime getOccupiedSince() {
        return occupiedSince;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public boolean hasCurrentSession() {
        return currentSession != null;
    }

    public void occupy(Vehicle vehicle, ParkingHistory session, LocalDateTime now) {
        if (occupied) {
            throw new IllegalStateException("이미 점유된 슬롯입니다.");
        }
        this.vehicle = vehicle;
        this.currentSession = session;
        this.occupied = true;
        this.occupiedSince = now;
        this.updatedAt = now;
    }

    public void release(LocalDateTime now) {
        if (!occupied) {
            return;
        }
        this.occupied = false;
        this.vehicle = null;
        this.currentSession = null;
        this.occupiedSince = null;
        this.updatedAt = now;
    }
}
