package com.transporthc.mapper.expenses;

import org.springframework.stereotype.Component;

import com.transporthc.dto.expenses.ExpensesAdvancesDto;
import com.transporthc.entity.expenses.ExpenseAdvancesEntity;

@Component
public class ExpennsesAdvancesMapper {
    public ExpenseAdvancesEntity toExpenseAdvancesEntity(ExpensesAdvancesDto dto){
        if(dto==null) return null;
        return ExpenseAdvancesEntity.builder()
        .driverId(dto.getDriverId())
        .period(dto.getPeriod())
        .advance(dto.getAdvance())
        .remainingBalance(0f)
        .note(dto.getNote())
        .build();
    }

    public void updateExpenseAdvance(Integer id, ExpenseAdvancesEntity expenseAdvances, ExpensesAdvancesDto dto) {
        if (dto == null || expenseAdvances == null) return;
        expenseAdvances.setId(id);
        expenseAdvances.setDriverId(dto.getDriverId());
        expenseAdvances.setPeriod(dto.getPeriod());
        expenseAdvances.setAdvance(dto.getAdvance());
        expenseAdvances.setRemainingBalance(dto.getRemainingBalance());
        expenseAdvances.setNote(dto.getNote());
    }
}
