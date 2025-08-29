package com.transporthc.mapper.schedule;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.transporthc.dto.schedule.ScheduleDto;
import com.transporthc.entity.schedule.Schedule;
import com.transporthc.enums.IDKey;
import com.transporthc.enums.schedule.ScheduleStatusEnum;
import com.transporthc.utils.utils;

@Component
public class ScheduleMapper {
    public Schedule toSchedule(ScheduleDto dto) {
        if (dto == null) return null;
        return Schedule.builder()
                .id(utils.genID(IDKey.SCHEDULE))
                .scheduleConfigId(dto.getScheduleConfigId().isBlank() ? null : dto.getScheduleConfigId().trim())
                .truckLicense(dto.getTruckLicense().trim())
                .moocLicense(dto.getMoocLicense().trim())
                .departureTime(dto.getDepartureTime())
                .note(dto.getNote().trim())
                .type(dto.getType().getValue())
                .status(ScheduleStatusEnum.PENDING.getValue())
                .build();
    }

    public List<Schedule> toScheduleList(List<ScheduleDto> dtos) {
        if (dtos == null || dtos.isEmpty()) {
            return Collections.emptyList();
        }
        return dtos.stream().map(this::toSchedule).collect(Collectors.toList());
    }

    public void updateSchedule(Schedule schedule, ScheduleDto dto) {
        if (dto == null) return;

        if (dto.getScheduleConfigId() != null && !schedule.getScheduleConfigId().equals(dto.getScheduleConfigId())) {
            schedule.setScheduleConfigId(dto.getScheduleConfigId());
        }
        if (dto.getTruckLicense() != null && !schedule.getTruckLicense().equals(dto.getTruckLicense())) {
            schedule.setTruckLicense(dto.getTruckLicense());
        }
        if (dto.getMoocLicense() != null && !schedule.getMoocLicense().equals(dto.getMoocLicense())) {
            schedule.setMoocLicense(dto.getMoocLicense());
        }
        if (dto.getDepartureTime() != null && !schedule.getDepartureTime().equals(dto.getDepartureTime())) {
            schedule.setDepartureTime(dto.getDepartureTime());
        }
        if (dto.getArrivalTime() != null && !schedule.getArrivalTime().equals(dto.getArrivalTime())) {
            schedule.setArrivalTime(dto.getArrivalTime());
        }
        if (dto.getNote() != null && !schedule.getNote().equals(dto.getNote())) {
            schedule.setNote(dto.getNote());
        }
        if (dto.getType() != null && !schedule.getType().equals(dto.getType().getValue())) {
            schedule.setType(dto.getType().getValue());
        }
    }
}
