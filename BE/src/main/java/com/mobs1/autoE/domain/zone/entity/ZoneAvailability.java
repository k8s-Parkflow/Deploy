package com.mobs1.autoE.domain.zone.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "ZONE_AVAILABILITY")
public class ZoneAvailability {

    @Id
    @Column(name = "zone_id")
    private Integer id;

    @OneToOne(optional = false)
    @MapsId
    @JoinColumn(name = "zone_id")
    private Zone zone;

    @Column(name = "total_slots", nullable = false)
    private int totalSlots;

    @Column(name = "occupied_slots", nullable = false)
    private int occupiedSlots;

    @Column(name = "available_slots", nullable = false)
    private int availableSlots;

    @Column(name = "general_total", nullable = false)
    private int generalTotal;

    @Column(name = "general_occupied", nullable = false)
    private int generalOccupied;

    @Column(name = "general_available", nullable = false)
    private int generalAvailable;

    @Column(name = "ev_total", nullable = false)
    private int evTotal;

    @Column(name = "ev_occupied", nullable = false)
    private int evOccupied;

    @Column(name = "ev_available", nullable = false)
    private int evAvailable;

    @Column(name = "disabled_total", nullable = false)
    private int disabledTotal;

    @Column(name = "disabled_occupied", nullable = false)
    private int disabledOccupied;

    @Column(name = "disabled_available", nullable = false)
    private int disabledAvailable;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    protected ZoneAvailability() {
        // JPA default constructor
    }

    public ZoneAvailability(Zone zone,
                            int totalSlots,
                            int occupiedSlots,
                            int availableSlots,
                            int generalTotal,
                            int generalOccupied,
                            int generalAvailable,
                            int evTotal,
                            int evOccupied,
                            int evAvailable,
                            int disabledTotal,
                            int disabledOccupied,
                            int disabledAvailable,
                            LocalDateTime updatedAt) {
        this.zone = zone;
        this.totalSlots = totalSlots;
        this.occupiedSlots = occupiedSlots;
        this.availableSlots = availableSlots;
        this.generalTotal = generalTotal;
        this.generalOccupied = generalOccupied;
        this.generalAvailable = generalAvailable;
        this.evTotal = evTotal;
        this.evOccupied = evOccupied;
        this.evAvailable = evAvailable;
        this.disabledTotal = disabledTotal;
        this.disabledOccupied = disabledOccupied;
        this.disabledAvailable = disabledAvailable;
        this.updatedAt = updatedAt;
    }

    //getter
    public Integer getId() {
        return id;
    }

    public Zone getZone() {
        return zone;
    }

    public int getTotalSlots() {
        return totalSlots;
    }

    public int getOccupiedSlots() {
        return occupiedSlots;
    }

    public int getAvailableSlots() {
        return availableSlots;
    }

    public int getGeneralTotal() {
        return generalTotal;
    }

    public int getGeneralOccupied() {
        return generalOccupied;
    }

    public int getGeneralAvailable() {
        return generalAvailable;
    }

    public int getEvTotal() {
        return evTotal;
    }

    public int getEvOccupied() {
        return evOccupied;
    }

    public int getEvAvailable() {
        return evAvailable;
    }

    public int getDisabledTotal() {
        return disabledTotal;
    }

    public int getDisabledOccupied() {
        return disabledOccupied;
    }

    public int getDisabledAvailable() {
        return disabledAvailable;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void refreshCounts(int totalSlots,
                              int occupiedSlots,
                              int availableSlots,
                              int generalTotal,
                              int generalOccupied,
                              int generalAvailable,
                              int evTotal,
                              int evOccupied,
                              int evAvailable,
                              int disabledTotal,
                              int disabledOccupied,
                              int disabledAvailable,
                              LocalDateTime updatedAt) {
        this.totalSlots = totalSlots;
        this.occupiedSlots = occupiedSlots;
        this.availableSlots = availableSlots;
        this.generalTotal = generalTotal;
        this.generalOccupied = generalOccupied;
        this.generalAvailable = generalAvailable;
        this.evTotal = evTotal;
        this.evOccupied = evOccupied;
        this.evAvailable = evAvailable;
        this.disabledTotal = disabledTotal;
        this.disabledOccupied = disabledOccupied;
        this.disabledAvailable = disabledAvailable;
        this.updatedAt = updatedAt;
    }
}
