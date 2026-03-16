package com.transporthc.service.expenses.expenseAdvances;

import java.util.List;

import com.transporthc.dto.expenses.ExpensesAdvancesDto;

public interface ExpenseAdvancesService {
    List<ExpensesAdvancesDto> getAll(int page);
    ExpensesAdvancesDto getByDriverId(String id);
    long delete(Integer id);
    ExpensesAdvancesDto update(Integer id, ExpensesAdvancesDto dto);
    ExpensesAdvancesDto create(ExpensesAdvancesDto dto);
}
