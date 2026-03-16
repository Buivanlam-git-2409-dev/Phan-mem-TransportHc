package com.transporthc.repository.schedule.schedule;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.transporthc.dto.schedule.ScheduleDto;
import com.transporthc.dto.schedule.ScheduleSalaryDto;
import com.transporthc.enums.schedule.ScheduleStatusEnum;

public interface ScheduleRepoCustom {
    List<ScheduleDto> getAll(int page, String driverId, String truckLicense, Date fromDate, Date toDate);
    List<ScheduleDto> getAll(String driverId, String truckLicense, Date fromDate, Date toDate);
    Optional<ScheduleDto> getByID(String id);
    long delete(String id);
    long approve(String id, boolean approved);
    long markComplete(String id);
    List<ScheduleSalaryDto> exportScheduleSalary(String driverId, Date fromDate, Date toDate);
    List<ScheduleDto> exportReport(String license, Date fromDate, Date toDate);
    long countByID(String id);
    ScheduleStatusEnum getStatusByID(String id);
    List<ScheduleDto> findByLicensePlate(String licensePlate);
}
