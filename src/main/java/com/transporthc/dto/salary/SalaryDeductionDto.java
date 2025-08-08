package com.transporthc.dto.salary;

import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class SalaryDeductionDto {
    private Float mandatoryInsurance;
    private Float tradeUnion;
    private Float advance;
    private Float errorOfDriver;
    private Float snn;
}
