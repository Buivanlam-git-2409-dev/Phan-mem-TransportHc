package com.transporthc.entity.transaction;

import com.transporthc.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transactions")
public class Transaction extends BaseEntity {
    @Id
    private String id;
    // tham chiếu với người dùng
    @Column(name = "ref_user_id")
    private String refUserId;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "products_id")
    private String productsId;

    @Column(name = "quantity")
    private Float quantity;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "transaction_time")
    private Date transactionTime;

    @Column(name = "origin")
    private Boolean origin;

    @Column(name = "destination")
    private String destination;

    @Column(name = "image")
    private String image;
}
