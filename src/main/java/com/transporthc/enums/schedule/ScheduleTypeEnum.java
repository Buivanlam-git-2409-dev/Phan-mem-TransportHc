package com.transporthc.enums.schedule;

import lombok.Getter;

@Getter
public enum ScheduleTypeEnum {
    INTERNAL(0, "Chạy nội bộ"),
    PAYROLL(1, "Chạy tính lương");

    private final Integer value;
    private final String description;

    ScheduleTypeEnum(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public static ScheduleTypeEnum valueOf(Integer value) {
        ScheduleTypeEnum[] values = ScheduleTypeEnum.values();
        for (ScheduleTypeEnum e : values) {
            if (e.value.equals(value)) {
                return e;
            }
        }
        throw new IllegalArgumentException("");
    }
}
