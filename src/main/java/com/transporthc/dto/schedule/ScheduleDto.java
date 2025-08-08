package com.transporthc.dto.schedule;

import java.util.Date;

import com.transporthc.annotations.ExportColumn;
import com.transporthc.enums.schedule.ScheduleStatusEnum;
import com.transporthc.enums.schedule.ScheduleTypeEnum;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleDto {
    @ExportColumn(name = "Mã")
    private String id;
    // thong tin cau hinh lich trinh, null neu chay noi bo
    private String scheduleConfigId;
    @ExportColumn(name = "Điểm đi")
    private String placeA;
    @ExportColumn(name = "Điểm đến")
    private String placeB;
    @ExportColumn(name ="Số tiền")
    private Float amount;

    @ExportColumn(name = "Mã tài xế")
    private String driverId;
    @ExportColumn(name = "Tên tài xế")
    private String driverName;

    @NotBlank(message = "Thông tin xe tải không được để trống!")
    @ExportColumn(name = "BS Xe")
    private String truckLicense;
    @NotBlank(message = "Thông tin rơ-mooc không được để trống!")
    private String moocLicense;

    // thoi gian khoi hanh/ hoan thanh
    @ExportColumn(name = "Ngày lấy hàng")
    private Date departureTime;
    @ExportColumn(name = "Ngày giao hàng")
    private Date arrivalTime;

    @ExportColumn(name = "Ghi chú")
    private String note;

    //Ảnh đính kèm
    private String[] attachedPaths = {};

    // loai hanh trinh: noi bo/ tinh luong
    @NotNull(message = "Loại hành trình không được để trống")
    private ScheduleTypeEnum type;

    // trang thai cua don
    private ScheduleStatusEnum status;
    private Integer count=1; // so chuyen phat sinh

    @ExportColumn(name = "Ngày tạo")
    private Date createdAt;
    private Date updatedAt;
    public ScheduleDto(String id, String scheduleConfigId, String placeA, String placeB, Float amount, String driverId,
            String driverName, String truckLicense,String moocLicense, Date departureTime,Date arrivalTime, String note,
            String attachedPaths,Integer type, Integer status,
            Date createdAt, Date updatedAt) {
        this.id = id;
        this.scheduleConfigId = scheduleConfigId;
        this.placeA = placeA;
        this.placeB = placeB;
        this.amount = amount;
        this.driverId = driverId;
        this.driverName = driverName;
        this.truckLicense = truckLicense;
        this.moocLicense = moocLicense;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.note = note;
        if(attachedPaths!=null){
            this.attachedPaths=attachedPaths.split(",");
        }
        this.type = ScheduleTypeEnum.valueOf(type);
        this.status = ScheduleStatusEnum.valueOf(status);
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    public ScheduleDto(String scheduleConfigId, String placeA, String placeB, String driverId, String driverName,
        String truckLicense, String moocLicense, Date departureTime,
        Date arrivalTime, Integer count) {
        this.scheduleConfigId = scheduleConfigId;
        this.placeA = placeA;
        this.placeB = placeB;
        this.driverId = driverId;
        this.driverName = driverName;
        this.truckLicense = truckLicense;
        this.moocLicense = moocLicense;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.count = count;
    }
}
