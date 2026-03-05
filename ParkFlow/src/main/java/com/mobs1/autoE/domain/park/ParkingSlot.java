package com.mobs1.autoE.domain.park;

import com.mobs1.autoE.domain.zone.entity.Zone;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "PARKING_SLOT",
        uniqueConstraints = @UniqueConstraint(name = "uk_zone_slot_code", columnNames = {"zone_id", "slot_code"}))
public class ParkingSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "slot_id")
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "zone_id", nullable = false)
    private Zone zone;

    @ManyToOne(optional = false)
    @JoinColumn(name = "slot_type_id", nullable = false)
    private SlotType slotType;

    @Column(name = "slot_code", nullable = false)
    private String slotCode;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    protected ParkingSlot() {
        // JPA default constructor
    }

    public ParkingSlot(Zone zone, SlotType slotType, String slotCode, boolean active) {
        this.zone = zone;
        this.slotType = slotType;
        this.slotCode = slotCode;
        this.active = active;
    }

    public ParkingSlot(Zone zone, SlotType slotType, String slotCode) {
        this(zone, slotType, slotCode, true);
    }

    // getter

    public Integer getId() {
        return id;
    }

    public Zone getZone() {
        return zone;
    }

    public SlotType getSlotType() {
        return slotType;
    }

    public String getSlotCode() {
        return slotCode;
    }

    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        if (!active) {
            throw new IllegalStateException("이미 비활성화된 슬롯입니다.");
        }
        this.active = false;
    }

    public void activate() {
        if (active) {
            throw new IllegalStateException("이미 활성화된 슬롯입니다.");
        }
        this.active = true;
    }
}
