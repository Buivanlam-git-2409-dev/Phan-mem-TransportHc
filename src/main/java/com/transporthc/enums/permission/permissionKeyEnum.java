package com.transporthc.enums.permission;

import lombok.Getter;

@Getter
public enum permissionKeyEnum {
    VIEW("can_view"),WRITE("can_write"),DELETE("can_delete"),APPROVE("can_approve");

    private final String column;

    private permissionKeyEnum(String column) {
        this.column = column;
    }
}
