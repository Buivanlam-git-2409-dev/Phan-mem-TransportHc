package com.transporthc.entity.schedule;

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
@Table(name = "schedule_configs")
public class ScheduleConfigEntity extends BaseEntity{
    @Id
    private String id;

    @Column(name = "place_a")
    private String placeA;

    @Column(name = "place_b")
    private String placeB;

    @Column(name = "amount")
    private Float amount;

    @Column(name = "note")
    private String note;
}
