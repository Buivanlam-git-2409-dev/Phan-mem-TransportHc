package com.transporthc.dto.expenses;

import com.transporthc.annotations.ExportColumn;
import com.transporthc.enums.expenses.ExpensesStatusEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class ExpensesDto {
    @ExportColumn(name = "Mã chi phí")
    private String id;

    private String driverId;
    @ExportColumn(name = "Tài xế")
    private String driverName;

    @NotBlank(message = "Loại chi phí không được để trống!")
    private String expensesConfigId;
    @ExportColumn(name = "Loại chi phí")
    private String expensesConfigType;

    @NotNull(message = "Số tiền không được để trống!")
    @ExportColumn(name = "Số tiền")
    private Float amount;

    @ExportColumn(name = "Ghi chú")
    private String note;

    @NotEmpty(message = "Vui lòng cung cấp ít nhất một ảnh minh chứng!")
    @ExportColumn(name = "Hóa đơn")
    private String[] attachedPaths = {};

    @NotBlank(message = "Thông tin lịch trình không được để trống!")
    @ExportColumn(name = "Mã hành trình")
    private String scheduleId;

    private ExpensesStatusEnum status;

    @ExportColumn(name = "Ngày tạo")
    private Date createdAt;
    private Date updatedAt;

    public ExpensesDto(String driverId, String driverName, String expensesConfigId, String expensesConfigType, Float amount) {
        this.driverId = driverId;
        this.driverName = driverName;
        this.expensesConfigId = expensesConfigId;
        this.expensesConfigType = expensesConfigType;
        this.amount = amount;
    }

    public ExpensesDto(String id, String driverId, String driverName, String expensesConfigId, String expensesConfigType, Float amount, String note, String attachedPaths, String scheduleId, Integer status, Date createdAt, Date updatedAt) {
        this.id = id;
        this.driverId = driverId;
        this.driverName = driverName;
        this.expensesConfigId = expensesConfigId;
        this.expensesConfigType = expensesConfigType;
        this.amount = amount;
        this.note = note;
        if (attachedPaths != null) {
            this.attachedPaths = attachedPaths.split(",");
        }
        this.scheduleId = scheduleId;
        this.status = ExpensesStatusEnum.valueOf(status);
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}