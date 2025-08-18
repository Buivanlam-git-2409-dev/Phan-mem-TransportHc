package com.transporthc.service.report;

import java.util.List;

import com.transporthc.dto.ExportExcelResponse;
import com.transporthc.dto.report.ReportDetailSalaryDto;
import com.transporthc.dto.report.SummarySalaryDto;

public interface ReportService {
    ReportDetailSalaryDto getReport(String userId, String period);
    List<SummarySalaryDto> getSummarySalaryReport(String period);
    ExportExcelResponse exportReport(String userId, String period) throws Exception;
    ExportExcelResponse exportSummarySalaryReport(String period) throws Exception;
}
