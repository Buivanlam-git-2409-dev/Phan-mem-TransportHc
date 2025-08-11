package com.transporthc.repository.report;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.transporthc.dto.report.ReportDetailSalaryDto;
import com.transporthc.dto.report.SummarySalaryDto;

@Component
public interface ReportRepo {
    ReportDetailSalaryDto getReport(String userId, String period, Date fromDate,Date toDate);
    List<SummarySalaryDto> getSummarySalary(String period, Date fromDate, Date todDate);
}
