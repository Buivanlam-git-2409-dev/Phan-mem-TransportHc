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
public class Truck extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    // id tài xế
    @Column(name = "driver_id",nullable = false)
    private String driverId;
    // biển số xe
    @Column(name = "license_plate",nullable = false)
    private String licensePlate;
    // dung tich xe
    @Column(name = "capacity",nullable = false)
    private Float capacity;

    @Column(name = "note")
    private String note;

    @Column(name = "status",nullable = false)
    private Integer status;
    // loại xe
    @Column(name = "type",nullable = false)
    private Integer type;
}
