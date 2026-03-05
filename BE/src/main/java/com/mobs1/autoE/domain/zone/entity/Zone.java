package com.mobs1.autoE.domain.zone.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "ZONE", uniqueConstraints = {
        @UniqueConstraint(name = "uk_zone_name", columnNames = "zone_name")
})
public class Zone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "zone_id")
    private Integer id;

    @Column(name = "zone_name", nullable = false, unique = true)
    private String name;

    protected Zone() {
        // JPA default constructor
    }

    //setter
    public Zone(String name) {
        this.name = name;
    }

    //getter
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void rename(String name) {
        this.name = name;
    }
}
