package com.transporthc.enums.attached;

import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;

@Getter
public enum AttachedTypeEnum {
    ATTACHED_OF_SCHEDULE(0,"Ảnh đính kèm của lịch trình"),
    ATTACHED_OF_EXPENSES(1,"Ảnh đính kèm của chi phí");

    private final Integer value;
    @JsonValue

    private final String description;
    private AttachedTypeEnum(Integer value, String description) {
        this.value = value;
        this.description = description;
    }
    public static AttachedTypeEnum valueOf(int value){
        AttachedTypeEnum[] values = AttachedTypeEnum.values();

        for(AttachedTypeEnum a: values){
            if(a.value.equals(value)) return a;
        }
        throw new IllegalArgumentException("");
    }
}
