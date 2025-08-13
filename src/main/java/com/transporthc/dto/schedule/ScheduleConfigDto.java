package com.transporthc.dto.schedule;

import java.util.Date;

import com.transporthc.annotations.ExportColumn;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter@Setter@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleConfigDto {
    @ExportColumn(name = "Mã HT")
    private String id;

    @NotBlank(message = "Thông tin địa chỉ điểm đi không được để trống!")
    @ExportColumn(name = "Điểm đi")
    private String placeA;
    @NotBlank(message = "Thông tin địa chỉ điểm đến không được để trống!")
    @ExportColumn(name = "Điểm đến")
    private String placeB;

    @NotNull(message = "Chi phí lịch trình không được để trống!")
    @ExportColumn(name = "Giá chuyến")
    private Float amount;

    @ExportColumn(name ="Ghi chú")
    private String note;

    private Date createdAt;
    private Date updatedAt;
}
