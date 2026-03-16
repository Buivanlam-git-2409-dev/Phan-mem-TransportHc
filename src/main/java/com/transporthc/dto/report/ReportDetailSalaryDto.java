package com.transporthc.dto.report;

import java.util.List;

import com.transporthc.dto.salary.SalaryDto;
import com.transporthc.dto.schedule.ScheduleSalaryDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportDetailSalaryDto {
    private SalaryDto Salary;
    private List<ScheduleSalaryDto> schedules;
}
