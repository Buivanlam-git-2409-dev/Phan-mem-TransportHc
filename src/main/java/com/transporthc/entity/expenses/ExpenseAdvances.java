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

public class ExpenseAdvances extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    // ma tai xe
    @Column(name = "driver_id")
    private String driverId;
    // ung truoc
    @Column(name = "period")
    private String period;
    // tien them
    @Column(name = "advance")
    private Float advance;
    // luong co ban
    @Column(name = "remaining_balance")
    private Float remainingBalance;
    // ghi chu
    @Column(name = "note")
    private String note;
}
