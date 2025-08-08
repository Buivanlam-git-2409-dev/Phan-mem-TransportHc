package com.transporthc.entity.expenses;

import com.transporthc.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "expenses_configs")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseConfigEntity extends BaseEntity {
    @Id
    private String id;

    @Column(name = "type")
    private String type;

    @Column(name = "note")
    private String note;
}
