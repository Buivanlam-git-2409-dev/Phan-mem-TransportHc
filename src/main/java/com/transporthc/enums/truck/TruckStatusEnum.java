package com.transporthc.enums.truck;

import lombok.Getter;

@Getter
public enum TruckStatusEnum {
    MAINTENANCE(-1,"Đang bảo trì"),
    APPROVED_SCHEDULE(0,"Lịch trình liên quan đã được duyệt"),
    AVAILABLE(1,"Có sẵn"),
    PENDING_SCHEDULE(2,"Lịch trình liên quan đang chờ xử lý");

    private final Integer value;
    private final String description;

    private TruckStatusEnum(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

    public static TruckStatusEnum valueOf(int value){
        TruckStatusEnum[] values = TruckStatusEnum.values();
        for( TruckStatusEnum status: values){
            if(status.value.equals(value)){
                return status;
            }
        }
        throw new IllegalArgumentException("");
    }
    
}