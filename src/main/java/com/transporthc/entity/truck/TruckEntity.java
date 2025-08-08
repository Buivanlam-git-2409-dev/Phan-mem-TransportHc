package com.transporthc.entity.truck;

import com.transporthc.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "trucks")
public class TruckEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "driver_id",nullable = false)
    private String driverId;

    @Column(name = "license_plate",nullable = false)
    private String licensePlate;

    @Column(name = "capacity",nullable = false)
    private Float capacity;

    @Column(name = "note")
    private String note;

    @Column(name = "status",nullable = false)
    private Integer status;

    @Column(name = "type",nullable = false)
    private Integer type;
}
