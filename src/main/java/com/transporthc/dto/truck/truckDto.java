package com.transporthc.dto.truck;

import java.util.Date;

import com.transporthc.annotations.ExportColumn;
import com.transporthc.enums.truck.TruckStatusEnum;
import com.transporthc.enums.truck.TruckTypeEnum;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TruckDto {
    @ExportColumn(name = "Mã")
    private Integer id;

    @Size(min = 8,message = "Biển số xe tối thiểu 8 ký tự")
    @ExportColumn(name = "Biển số")
    private String licensePlate;

    @NotNull(message = "Dung tích xe không được để trống")
    @ExportColumn(name = "Dung tích")
    private Float capacity;

    @NotNull(message = "ID tài xế không được để trống")
    private String driverId;
    @ExportColumn(name = "Tài xế")
    private String driverName;

    @NotNull(message = "Loại xe không được để trống")
    private TruckTypeEnum type;
    @ExportColumn(name = "Loại")
    private String typeDescription;

    private TruckStatusEnum status;
    @ExportColumn(name = "Trạng thái")
    private String statusDescription;

    @ExportColumn(name = "Ghi chú")
    private String note;

    @ExportColumn(name = "Ngày tạo")
    private Date createAt;
    private Date updateAt;
    public TruckDto(Integer id, @Size(min = 8, message = "Biển số xe tối thiểu 8 ký tự") String licensePlate,
            @NotNull(message = "Dung tích xe không được để trống") Float capacity,
            @NotNull(message = "ID tài xế không được để trống") String driverId, String driverName, int type,
            String typeDescription, int status, String statusDescription, String note, Date createAt,
            Date updateAt) {
        this.id = id;
        this.licensePlate = licensePlate;
        this.capacity = capacity;
        this.driverId = driverId;
        this.driverName = driverName;
        this.type = TruckTypeEnum.valueOf(type);
        this.typeDescription = typeDescription;
        this.status = TruckStatusEnum.valueOf(status);
        this.statusDescription = statusDescription;
        this.note = note;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }

    
}
