package com.transporthc.mapper.schedule;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.transporthc.dto.schedule.ScheduleConfigDto;
import com.transporthc.entity.schedule.ScheduleConfig;
import com.transporthc.enums.IDKey;
import com.transporthc.utils.utils;

@Component
public class ScheduleConfigMapper {
    public ScheduleConfig toScheduleConfig(ScheduleConfigDto dto) {
        if (dto == null) return null;
        return ScheduleConfig.builder()
                .id(utils.genID(IDKey.SCHEDULE_CONFIG))
                .placeA(dto.getPlaceA())
                .placeB(dto.getPlaceB())
                .amount(dto.getAmount())
                .note(dto.getNote())
                .build();
    }

    public List<ScheduleConfig> toScheduleConfigList(List<ScheduleConfigDto> dtos) {
        if(dtos == null || dtos.isEmpty()) {
            return Collections.emptyList();
        }
        return dtos.stream().map(this::toScheduleConfig).collect(Collectors.toList());
    }

    public void updateScheduleConfig(ScheduleConfig config, ScheduleConfigDto dto) {
        if (dto == null) return;

        Field[] fields = dto.getClass().getDeclaredFields();
        for (Field srcField : fields) {
            srcField.setAccessible(true);
            try {
                Object newValue = srcField.get(dto);
                if (newValue != null) {
                    Field targetField = config.getClass().getField(srcField.getName());
                    targetField.setAccessible(true);
                    if (!newValue.equals(targetField.get(config))) {
                        targetField.set(config, newValue);
                    }
                    targetField.setAccessible(false);
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                //ignore
            } finally {
                srcField.setAccessible(false);
            }
        }
    }
}
