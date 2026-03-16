package com.transporthc.enums.truck;

import lombok.Getter;

@Getter
public enum TruckTypeEnum {
    TRUCK_HEAD(0,"Đầu xe tải"),
    MOOC(1,"Rơ-mooc");

    private final Integer value;
    private final String description;
    private TruckTypeEnum(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

    public static TruckTypeEnum valueOf(int value){
        TruckTypeEnum[] values = TruckTypeEnum.values();

        for(TruckTypeEnum type: values){
            if(type.value.equals(value)){
                return type;
            }
        }
        throw new IllegalArgumentException("");
    }
}
