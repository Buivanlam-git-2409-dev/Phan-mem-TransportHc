package com.transporthc.service.expenses.expensesConfig;

import com.transporthc.dto.ExportExcelResponse;
import com.transporthc.dto.expenses.ExpensesConfigDto;
import com.transporthc.entity.expenses.ExpenseConfigEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ExpensesConfigService {
    List<ExpensesConfigDto> getAll();
    ExpensesConfigDto getByID(String id);
    ExpensesConfigDto create(ExpensesConfigDto dto);
    ExpensesConfigDto update(String id, ExpensesConfigDto dto);
    long deleteByID(String id);
    List<ExpenseConfigEntity> importExpensesConfigData(MultipartFile importFile);
    ExportExcelResponse exportExpensesConfig() throws Exception;
}