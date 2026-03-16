package com.transporthc.enums.role;

import lombok.Getter;

@Getter
public enum UserRoleEnum {
    ADMIN(1),
    ACCOUNTANT(2),
    MANAGER(3),
    DRIVER(4);

    private final Integer id;

    UserRoleEnum(int id) {
        this.id = id;
    }

    public static UserRoleEnum valueOf(Integer value) {
        UserRoleEnum[] values = UserRoleEnum.values();
        for (UserRoleEnum roleName : values) {
            if (roleName.id.equals(value)) {
                return roleName;
            }
        }
        throw new IllegalArgumentException("");
    }
}
