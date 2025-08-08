package com.transporthc.enums.permission;

import lombok.Getter;

@Getter
public enum permissionTypeEnum {
    USERS("Quản lý người dùng", "USERS"),
    PERMISSIONS("Phân quyền", "PERMISSIONS"),
    CONFIGS("Quản lý cấu hình", "CONFIGS"),
    TRUCKS("Quản lý xe tải", "TRUCKS"),
    EXPENSES("Quản lý chi phí", "EXPENSES"),
    SCHEDULES("Quản lý lịch trình", "SCHEDULES"),
    SALARIES("Quản lý lương", "SALARIES"),
    TRANSACTIONS("Quản lý giao dịch", "TRANSACTIONS"),
    REPORTS("Báo cáo", "REPORTS"),
    WAREHOUSES("Quản lý kho", "WAREHOUSES"),
    GOODS("Quản lý hàng hóa", "GOODS"),
    TRANSACTION("Quản lý giao dịch", "TRANSACTION");
    
    private final String title;
    private final String name;

    permissionTypeEnum(String title, String name){
        this.title = title;
        this.name = name;
    }
}
