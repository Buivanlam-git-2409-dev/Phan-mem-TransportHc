package com.transporthc.enums.expenses;

import lombok.Getter;

@Getter
public enum ExpensesStatusEnum {
    PENDING(0,"Chờ xử lý"),
    APPROVE(1,"Đã duyệt");

    private final Integer value;
    private final String desciption;

    ExpensesStatusEnum(int value,String desciption){
        this.value=value;
        this.desciption=desciption;
    }

    public static ExpensesStatusEnum valueOf(int value){
        ExpensesStatusEnum[] values = ExpensesStatusEnum.values();
        for(ExpensesStatusEnum status: values){
            if(status.value.equals(value)){
                return status;
            }
        }
        throw new IllegalArgumentException("");
    }
}
