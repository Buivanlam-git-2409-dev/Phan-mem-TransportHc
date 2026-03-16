package com.transporthc.service.schedule.scheduleconfig;

import com.transporthc.dto.ExportExcelResponse;
import com.transporthc.dto.schedule.ScheduleConfigDto;
import com.transporthc.entity.schedule.ScheduleConfig;
import org.springframework.web.multipart.MultipartFile;

import java.rmi.ServerException;
import java.util.List;

public interface ScheduleConfigService {
    List<ScheduleConfigDto> getAll(int page);
    List<ScheduleConfigDto> getAll();
    ScheduleConfigDto getByID(String id);
    ScheduleConfigDto create(ScheduleConfigDto dto);
    ScheduleConfigDto update(String id, ScheduleConfigDto dto);
    long deleteByID(String id) throws ServerException;
    List<ScheduleConfig> importScheduleConfigData(MultipartFile importFile);
    ExportExcelResponse exportScheduleConfig() throws Exception;
}