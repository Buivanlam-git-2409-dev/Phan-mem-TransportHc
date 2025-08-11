package com.transporthc.dto.report;
import com.transporthc.annotations.ExportColumn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SummarySalaryDto {
    @ExportColumn(name = "Mã NV")
    private String userId;
    @ExportColumn(name = "Họ tên")
    private String name;
    @ExportColumn(name = "Lương chuyến")
    private Float sumTotalSchedules;
    @ExportColumn(name = "Lương khác")
    private Float sumSalaryDeduction;
    @ExportColumn(name = "Khoản trừ")
    private Float sumSalaryReceived;
    @ExportColumn(name = "Thực nhận")
    private Float netSalary;
}