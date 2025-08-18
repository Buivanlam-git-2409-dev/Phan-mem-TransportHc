package com.transporthc.service.schedule.schedule;

import com.transporthc.dto.attached.AttachedImgPathDto;
import com.transporthc.dto.ExportExcelResponse;
import com.transporthc.dto.schedule.ScheduleDto;
import com.transporthc.dto.schedule.ScheduleSalaryDto;
import com.transporthc.entity.schedule.ScheduleEntity;
import org.springframework.web.multipart.MultipartFile;

import java.rmi.ServerException;
import java.util.List;
public interface ScheduleService {
    List<ScheduleDto> getAll(Integer page, String driverId, String truckLicense, String fromDateStr, String toDateStr);
    ScheduleDto getByID(String id);
    ScheduleDto create(ScheduleDto dto) throws ServerException;
    ScheduleDto update(String id, ScheduleDto dto);
    long deleteByID(String id) throws ServerException;
    long approveByID(String id, boolean approved) throws ServerException;
    long markComplete(String id, AttachedImgPathDto attachedImgPathsDto) throws ServerException;
    List<ScheduleDto> report(String license, String period);
    List<ScheduleSalaryDto> exportScheduleSalary (String driverId, String period);
    List<ScheduleEntity> importScheduleData(MultipartFile importFile);
    ExportExcelResponse exportSchedule(String driverId, String truckLicense, String fromDate, String toDate) throws Exception;
}
