package com.mobs1.autoE.domain.park;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "SLOT_TYPE", uniqueConstraints = {
        @UniqueConstraint(name = "uk_slot_type_name", columnNames = "type")
})
public class SlotType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "slot_type_id")
    private Integer id;

    @Column(name = "type", nullable = false, unique = true)
    private String type;

    protected SlotType() {
        // JPA default constructor
    }

    public SlotType(String type) {
        this.type = type;
    }

    // getter
    public Integer getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public void changeType(String type) {
        this.type = type;
    }
}
