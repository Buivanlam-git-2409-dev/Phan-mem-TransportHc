package com.transporthc.entity.expenses;

import com.transporthc.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "expense_advances")

public class ExpenseAdvancesEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "driver_id")
    private String driverId;

    @Column(name = "period")
    private String period;

    @Column(name = "advance")
    private Float advance;

    @Column(name = "remaining_balance")
    private Float remainingBalance;

    @Column(name = "note")
    private String note;
}
