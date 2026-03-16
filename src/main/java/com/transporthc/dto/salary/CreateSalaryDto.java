package com.transporthc.dto.salary;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class CreateSalaryDto {
    private String userId;
    private Float phoneAllowance;
    private Float basicSalary;
    private Float jobAllowance;
    private Float bonus;
    private Float monthlyPaidLeave;
    private Float ot;
    private Float receivedSnn;
    private Float unionContribution;
    private Float travelExpensesReimbursement;
    private Float advance;
    private Float errorOfDriver;
    private Float deductionSnn;
}
