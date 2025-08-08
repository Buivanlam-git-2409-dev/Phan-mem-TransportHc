package com.transporthc.dto.expenses;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateExpensesDto {
    private String driverId;
    private String driverName;

    private String expensesConfigId;
    private String expensesConfigType;

    private Float amount;
    private String note;
    private String imgPath;
    private String scheduleId;
    private Integer status;
    
    private Date createdAt;
    private Date updateAt;
}
