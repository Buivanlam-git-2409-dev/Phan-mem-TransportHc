package com.transporthc.entity.products;

import com.transporthc.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductsReport extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "products_id")
    private String productsId;

    @Column(name = "beginning_inventory")
    private Float beginningInventory;

    @Column(name = "ending_inventory")
    private Float endingInventory;
}
