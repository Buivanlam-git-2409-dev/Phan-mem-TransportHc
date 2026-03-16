package com.transporthc.controller.report;

import com.transporthc.dto.BaseResponse;
import com.transporthc.dto.ExportExcelResponse;
import com.transporthc.dto.report.ReportDetailSalaryDto;
import com.transporthc.dto.report.SummarySalaryDto;
import com.transporthc.service.report.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/report")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @GetMapping("/detail-salary-report")
    public ResponseEntity<ReportDetailSalaryDto> getDetailSalary(@RequestParam String userId, @RequestParam String period) {
        return ResponseEntity.ok(
                BaseResponse.ok(reportService.getReport(userId, period)).getData()
        );
    }

    @GetMapping("/summary-salary-report")
    public ResponseEntity<BaseResponse<List<SummarySalaryDto>>> getSumarySalaryReport(@RequestParam String period) {
        return ResponseEntity.ok(BaseResponse.ok(reportService.getSummarySalaryReport(period)));
    }

    @GetMapping("/export/summary-salary-report")
    public ResponseEntity<Object> exportReportSummarySalary(
            @RequestParam String period) throws Exception {

        ExportExcelResponse exportExcelResponse = reportService.exportSummarySalaryReport(period);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=" + URLEncoder.encode(exportExcelResponse.getFilename(), StandardCharsets.UTF_8)
                )
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel; charset=UTF-8"))
                .body(exportExcelResponse.getResource());
    }

    @GetMapping("/export/detail-salary-report")
    public ResponseEntity<Object> exportReportDetailSalary(
            @RequestParam String userId,
            @RequestParam String period
    ) throws Exception {

        ExportExcelResponse exportExcelResponse = reportService.exportReport(period, userId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=" + URLEncoder.encode(exportExcelResponse.getFilename(), StandardCharsets.UTF_8))
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel; charset=UTF-8"))
                .body(exportExcelResponse.getResource());
    }
}