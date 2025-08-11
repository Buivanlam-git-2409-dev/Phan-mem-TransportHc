package com.transporthc.repository.schedule.scheduleconfig;

import java.util.List;
import java.util.Optional;

import com.transporthc.dto.schedule.ScheduleConfigDto;

public interface ScheduleConfigRepoCustom {
    List<ScheduleConfigDto> getAll(int page);
    List<ScheduleConfigDto> getAll();
    Optional<ScheduleConfigDto> getByID(String id);
    long delete(String id);
    long countByID(String id);
}
