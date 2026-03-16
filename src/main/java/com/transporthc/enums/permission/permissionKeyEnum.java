package com.transporthc.enums.permission;

import lombok.Getter;

@Getter
public enum PermissionKeyEnum {
    VIEW("can_view"),WRITE("can_write"),DELETE("can_delete"),APPROVE("can_approve");

    private final String column;

    private PermissionKeyEnum(String column) {
        this.column = column;
    }
}
