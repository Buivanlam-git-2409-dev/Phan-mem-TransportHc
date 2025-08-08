package com.transporthc.dto.salary;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalaryReceivedDto {
    private Float phoneAllowance;
    private Float basicSalary;
    private Float jobAllowance;
    private Float bonus;
    private Float monthlyPaidLeave;
    private Float ot;
    private Float snn;
    private Float unionContribution;
    private Float travelExpensesReimbursement;
}
