package com.transporthc.entity.warehouse;

import com.transporthc.entity.BaseEntity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarehousesEntity extends BaseEntity{
    @Id
    private String id;

    @Column(name = "name",nullable = false,unique = true)
    private String name;
    
    @Column(name = "notes")
    private String notes;
}
