package com.transporthc.enums.schedule;

import lombok.Getter;

@Getter
public enum ScheduleStatusEnum {
    REJECTED(-1, "Không duyệt/Bị từ chối"),
    PENDING(0, "Đang chờ duyệt"),
    APPROVED(1, "Đã duyệt"),
    COMPLETED(2, "Đã hoàn thành");

    private final int value;
    private final String title;

    ScheduleStatusEnum(int value, String title) {
        this.value = value;
        this.title = title;
    }

    public static ScheduleStatusEnum valueOf(int value) {
        ScheduleStatusEnum[] values = ScheduleStatusEnum.values();
        for (ScheduleStatusEnum status : values) {
            if (status.value == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("");
    }
}
