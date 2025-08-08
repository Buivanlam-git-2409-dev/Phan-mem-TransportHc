package com.transporthc.repository.expenses.expensesconfig;

import java.util.List;
import java.util.Optional;

import com.transporthc.dto.expenses.ExpensesConfigDto;

public interface ExpensesConfigRepoCustom {
    List<ExpensesConfigDto> getAll();
    Optional<ExpensesConfigDto> getByID(String id);
    long delete(String id);
}
