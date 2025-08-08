package com.transporthc.repository.expenses.expenseadvances;

import java.util.List;
import java.util.Optional;

import com.transporthc.dto.expenses.ExpensesAdvancesDto;

public interface ExpenseAdvancesRepoCustom {
    List<ExpensesAdvancesDto> getAll(int page);
    Optional<ExpensesAdvancesDto> getByDriverId(String id);
    long deleted(Integer id);
    Optional<ExpensesAdvancesDto> getExpenseAdvanceById(Integer id);
}
