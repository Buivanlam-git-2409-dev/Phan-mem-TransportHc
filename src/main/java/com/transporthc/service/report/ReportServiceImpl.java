package com.transporthc.service.report;

import com.transporthc.dto.ExportExcelResponse;
import com.transporthc.dto.report.ReportDetailSalaryDto;
import com.transporthc.dto.report.SummarySalaryDto;
import com.transporthc.enums.permission.PermissionKeyEnum;
import com.transporthc.enums.permission.PermissionTypeEnum;
import com.transporthc.exception.define.InvalidParameterException;
import com.transporthc.exception.define.NotFoundException;
import com.transporthc.repository.report.ReportRepo;
import com.transporthc.service.BaseService;
import com.transporthc.utils.ExcelUtils;
import com.transporthc.utils.ExportConfig;
import com.transporthc.utils.utils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl extends BaseService implements ReportService {
    private final ReportRepo reportRepo;
    private final PermissionTypeEnum type = PermissionTypeEnum.REPORTS;

    @Override
    public ReportDetailSalaryDto getReport(String userId, String period) {
        checkPermission(type, PermissionKeyEnum.VIEW);
        if (userId.isEmpty()) {
            throw new InvalidParameterException("UserId không hợp lệ!");
        }
        Date[] range = utils.createDateRange(period);
        return reportRepo.getReport(userId, period, range[0], range[1]);
    }

    public List<SummarySalaryDto> getSummarySalaryReport(String period) {
        checkPermission(type, PermissionKeyEnum.VIEW);
        Date[] range = utils.createDateRange(period);
        return reportRepo.getSummarySalary(period, range[0], range[1]);
    }

    @Override
    public ExportExcelResponse exportReport(String userId, String period) throws Exception {
        checkPermission(type, PermissionKeyEnum.VIEW);
        Date[] range = utils.createDateRange(period);
        ReportDetailSalaryDto detailSalaryReport = reportRepo.getReport(userId, period, range[0], range[1]);

        if (detailSalaryReport == null ||
                (detailSalaryReport.getSalary() == null && CollectionUtils.isEmpty(detailSalaryReport.getSchedules()))) {
            throw new Exception("No data available for the specified user and period.");
        }

        String fileName = "DetailSalaryReport_" + userId + "_" + period + ".xlsx";

        ByteArrayInputStream in = ExcelUtils.export(List.of(detailSalaryReport), fileName, null);
        InputStreamResource inputStreamResource = new InputStreamResource(in);
        return new ExportExcelResponse(fileName, inputStreamResource);
    }

    @Override
    public ExportExcelResponse exportSummarySalaryReport(String period) throws Exception {
        checkPermission(type, PermissionKeyEnum.VIEW);
        Date[] range = utils.createDateRange(period);
        List<SummarySalaryDto> summarySalaryReport = reportRepo.getSummarySalary(period, range[0], range[1]);

        if (CollectionUtils.isEmpty(summarySalaryReport)) {
            throw new NotFoundException("No data");
        }
        String fileName = "SummarySalary Export" + ".xlsx";

        ByteArrayInputStream in = ExcelUtils.export(summarySalaryReport, fileName, ExportConfig.summarySalaryExport);

        InputStreamResource inputStreamResource = new InputStreamResource(in);
        return new ExportExcelResponse(fileName, inputStreamResource);
    }
}