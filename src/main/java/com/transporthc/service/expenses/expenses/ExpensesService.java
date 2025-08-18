package com.transporthc.service.expenses.expenses;

import java.rmi.ServerException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.transporthc.dto.ExportExcelResponse;
import com.transporthc.dto.expenses.ExpensesDto;
import com.transporthc.dto.expenses.ExpensesIncurredDto;
import com.transporthc.dto.expenses.ExpensesReportDto;
import com.transporthc.entity.expenses.ExpensesEntity;

public interface ExpensesService {
    List<ExpensesDto> getAll(Integer page, String expensesConfigId, String truckLicense, String fromDateStr, String toDateStr);
    ExpensesDto getByID(String id);
    ExpensesDto create(ExpensesDto dto) throws ServerException;
    ExpensesDto update(String id, ExpensesDto dto);
    long deleteByID(String id) throws ServerException;
    long approveByID(String id) throws ServerException;
    List<ExpensesIncurredDto> report(String driverId, String period);
    List<ExpensesReportDto> reportAll(String period);
    List<ExpensesEntity> importExpensesData(MultipartFile importFile);
    ExportExcelResponse exportExpenses(String expensesConfigId, String truckLicense, String fromDate, String toDate) throws Exception;
    ExportExcelResponse exportReportExpenses(String driverId, String period) throws Exception;
}
