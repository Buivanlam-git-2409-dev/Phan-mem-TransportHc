package com.transporthc.enums;

public enum IDKey {
    EXPENSES("EX"),
    EXPENSES_CONFIG("EXC"),
    SCHEDULE("SCD"),
    SCHEDULE_CONFIG("SCDC"),
    USER("US"),
    WAREHOUSE("WH"),
    PRODUCTS("PS"),
    TRANSACTION("TRANS");

    public final String label;

    private IDKey(String label) {
        this.label = label;
    }
}
