package com.transporthc.enums;

import lombok.Getter;

@Getter
public enum PeriodFormatter {
    YMD_FORMATTER("yyyy-MM-dd"),
    YM_FORMATTER("yyyy-MM");

    private String pattern;

    PeriodFormatter(String pattern) {
        this.pattern = pattern;
    }
}
