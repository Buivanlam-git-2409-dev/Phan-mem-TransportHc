package com.transporthc.dto.salary;

import com.transporthc.annotations.ExportColumn;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter@Builder
public class SalaryDto {
    @ExportColumn(name = "Phụ cấp điện thoại")
    private Float phoneAllowance;
    @ExportColumn(name = "Lương cơ bản")
    private Float basicSalary;
    @ExportColumn(name = "Phụ cấp công việc")
    private Float jobAllowance;
    @ExportColumn(name = "Thưởng")
    private Float bonus;
    @ExportColumn(name = "Lương ngày nghỉ trong tháng")
    private Float monthlyPaidLeave;
    @ExportColumn(name = "Lương đi làm ngày nghỉ Lễ/Tết")
    private Float ot;
    @ExportColumn(name = "Bảo hiểm chi trả ốm đau/thai sản")
    private Float receivedSnn;
    @ExportColumn(name = "Công đoàn công ty")
    private Float unionContribution;
    @ExportColumn(name = "Thanh toán tiền đi đường")
    private Float travelExpensesReimbursement;
    @ExportColumn(name = "Bảo hiểm bắt buộc (10,5%)")
    private Float mandatoryInsurance;
    @ExportColumn(name = "Kinh phí công đoàn (1%)")
    private Float tradeUnion;
    @ExportColumn(name = "Tổng tạm ứng lương")
    private Float advance;
    @ExportColumn(name = "Trừ phạt tiền do lỗi tài xế")
    private Float errorOfDriver;
    @ExportColumn(name = "Truy thu BHYT/BHXH")
    private Float deductionSnn;
}
