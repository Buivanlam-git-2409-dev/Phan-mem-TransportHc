package com.transporthc.dto.expenses;

import com.transporthc.annotations.ExportColumn;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExpensesIncurredDto {
    private String expenseConfigId;
    @ExportColumn(name = "Loại chi phí")
    private String type;
    @ExportColumn(name = "Số tiền")
    private Float amount;
}
