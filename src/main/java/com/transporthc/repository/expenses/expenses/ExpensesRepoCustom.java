package com.transporthc.repository.expenses.expenses;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.transporthc.dto.expenses.ExpensesDto;
import com.transporthc.dto.expenses.ExpensesIncurredDto;
import com.transporthc.dto.expenses.ExpensesReportDto;
import com.transporthc.enums.expenses.ExpensesStatusEnum;



public interface ExpensesRepoCustom {
    List<ExpensesDto> getAll(int page, String expensesConfigId, String truckLicense, Date fromDate, Date toDate);
    List<ExpensesDto> getAll(String expensesConfigId, String truckLicense, Date fromDate, Date toDate);
    List<ExpensesIncurredDto> getExpenseIncurredByDriverID(String driverId, Date fromDate, Date toDate);
    Optional<ExpensesDto> getByID(String id);
    long delete(String id);
    long approve(String id);
    List<ExpensesReportDto> reportAll(String period);
    long countByID(String id);
    ExpensesStatusEnum getStatusByID(String id);
}
