package com.transporthc.dto.schedule;

import com.transporthc.annotations.ExportColumn;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter@Setter@Builder@NoArgsConstructor
public class ScheduleSalaryDto {
    @ExportColumn(name = "Điểm đi")
    private String placeA;
    @ExportColumn(name = "Điểm đến")
    private String placeB;
    @ExportColumn(name = "Đơn giá")
    private Float amount; //Gia tien 1 chuyen
    @ExportColumn(name = "Số chuyến")
    private Integer count;
    @ExportColumn(name = "Thành tiền")
    private Float total;

    public ScheduleSalaryDto(String placeA, String placeB, Float amount, Integer count, Float total) {
        this.placeA = placeA;
        this.placeB = placeB;
        this.amount = amount;
        this.count = count;
        this.total = total;
    }
}
