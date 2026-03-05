package com.mobs1.autoE.domain.zone.entity;

import com.mobs1.autoE.global.enums.SlotCategory;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "VEHICLE")
public class Vehicle {

    @Id
    @Column(name = "vehicle_num", nullable = false)
    private String vehicleNum;

    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_type_code")
    private SlotCategory vehicleTypeCode;

    protected Vehicle() {
        // JPA default constructor
    }

    public Vehicle(String vehicleNum, SlotCategory vehicleTypeCode) {
        this.vehicleNum = vehicleNum;
        this.vehicleTypeCode = vehicleTypeCode;
    }

    //getter
    public String getVehicleNum() {
        return vehicleNum;
    }

    public SlotCategory getVehicleTypeCode() {
        return vehicleTypeCode;
    }

    public void changeType(SlotCategory vehicleTypeCode) {
        if (vehicleTypeCode == null) {
            throw new IllegalArgumentException("차량 타입 코드는 null이 될 수 없습니다.");
        }
        this.vehicleTypeCode = vehicleTypeCode;
    }
}
