package com.transporthc.entity.schedule;

import java.util.Date;

import com.transporthc.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "schedules")
public class Schedule extends BaseEntity{
    @Id
    private String id;

    @Column(name = "schedule_config_id")
    private String scheduleConfigId;

    @Column(name = "truck_license")
    private String truckLicense;

    @Column(name = "mooc_license")
    private String moocLicense;

    @Column(name = "departure_time")
    private Date departureTime;

    @Column(name = "arrival_time")
    private Date arrivalTime;

    @Column(name = "note")
    private String note;

    @Column(name = "type")
    private Integer type;

    @Column(name = "status")
    private Integer status;
}
