package com.transporthc.dto.transaction;

import java.util.Date;

import com.transporthc.annotations.ExportColumn;
import com.transporthc.enums.transaction.TransactionTypeEnum;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {
    private String id;

    @NotBlank(message = "ID người kiểm duyệt không được để trống")
    private String refUserId;
    private String fullNameRefUser;

    @NotBlank(message = "ID hàng hóa không được để trống")
    @ExportColumn(name = "Mã hàng hóa")
    private String goodsId;
    @ExportColumn(name = "Tên hàng hóa")
    private String goodsName;

    @NotNull(message = "Số lượng hàng hóa không được để trống")
    @ExportColumn(name = "KL (Tấn)")
    private Float quantity;

    @ExportColumn(name = "Ghi chú")
    private String destination;

    @ExportColumn(name = "Khách hàng")
    private String customerName;

    @ExportColumn(name = "Ngày giao dịch")
    private Date  transactionTime;

    @NotNull(message = "Loại giao dịch không được để trống")
    private TransactionTypeEnum origin;
    @ExportColumn(name = "Loại giao dịch")
    private String originDescription;

    private String image;

    public TransactionDto(String id, String refUserId, String fullNameRefUser, String goodsId, String goodsName, Float quantity, String destination, String customerName, Date transactionTime, Boolean origin, String originDescription, String image, Date createdAt, Date updatedAt) {
        this.id = id;
        this.refUserId = refUserId;
        this.fullNameRefUser = fullNameRefUser;
        this.goodsId = goodsId;
        this.goodsName = goodsName;
        this.quantity = quantity;
        this.destination = destination;
        this.customerName = customerName;
        this.transactionTime = transactionTime;
        this.origin = TransactionTypeEnum.valueOf(origin);
        this.originDescription = originDescription;
        this.image = image;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    private Date createdAt;

    private Date updatedAt;
}
