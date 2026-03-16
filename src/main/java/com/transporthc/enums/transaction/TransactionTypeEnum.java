package com.transporthc.enums.transaction;

import lombok.Getter;

@Getter
public enum TransactionTypeEnum {
    INBOUND_TRANSACTION(false,"Nhập hàng"),
    OUTBOUND_TRANSACTION(true,"Xuất hàng");

    private final Boolean value;
    private final String title;

    private TransactionTypeEnum(Boolean value, String title) {
        this.value = value;
        this.title = title;
    }
    public static TransactionTypeEnum valueOf(Boolean value){
        if(value) return OUTBOUND_TRANSACTION;
        else return INBOUND_TRANSACTION;
    }
}
