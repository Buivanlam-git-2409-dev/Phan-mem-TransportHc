package com.transporthc.entity.products;

import com.transporthc.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "products")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class Products extends BaseEntity{
    @Id
    private String id;

    @Column(name = "warehouse_id")
    private String warehouseId;

    @Column(name = "name",nullable = false)
    private String name;

    @Column(name = "quantity")
    private Float quantity;

    @Column(name = "amount")
    private Float amount;
}