package com.transporthc.dto.expenses;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpensesReportDto {
    // thong tin tai xe
    private String driverId;
    private String driverName;

    // thog tin xe
    private String truckLicense;
    private String mooLicense;

    // du dau ky  (du cuoi ky cua chu ky truoc)
    private Float prevRemainingBalance;

    // ung trong ky
    private Float advance;

    // cac chi phi phat sinh
    private List<ExpensesIncurredDto> expensesIncurred;
    // du cuoi ky
    private Float remainingBalance;
    public ExpensesReportDto(String driverId, String driverName, String truckLicense, String mooLicense,
            Float prevRemainingBalance, Float advance, Float remainingBalance) {
        this.driverId = driverId;
        this.driverName = driverName;
        this.truckLicense = truckLicense;
        this.mooLicense = mooLicense;
        this.prevRemainingBalance = prevRemainingBalance;
        this.advance = advance;
        this.expensesIncurred=null;
        this.remainingBalance = remainingBalance;
    }

    
}
