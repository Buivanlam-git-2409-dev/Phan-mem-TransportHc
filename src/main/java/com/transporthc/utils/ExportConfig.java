package com.transporthc.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.transporthc.annotations.ExportColumn;
import com.transporthc.dto.expenses.ExpensesConfigDto;
import com.transporthc.dto.expenses.ExpensesDto;
import com.transporthc.dto.expenses.ExpensesIncurredDto;
import com.transporthc.dto.products.ProductsReportDto;
import com.transporthc.dto.report.SummarySalaryDto;
import com.transporthc.dto.schedule.ScheduleDto;
import com.transporthc.dto.transaction.TransactionDto;
import com.transporthc.dto.truck.TruckDto;
import com.transporthc.dto.user.UserDto;
import com.transporthc.dto.warehouse.WarehousesDto;
import com.transporthc.entity.schedule.ScheduleConfigEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExportConfig {
    private int sheetIndex;
    private int startRow;
    private Class dataClass;
    private List<CellConfig> cellExportConfigList;
    
    public static ExportConfig createExportConfig(Class<?> dataClass,int sheetIndex,int startRow){
        ExportConfig exportConfig = new ExportConfig();
        exportConfig.setSheetIndex(sheetIndex);
        exportConfig.setStartRow(startRow);
        exportConfig.setDataClass(dataClass);
        
        List<CellConfig> cellConfigList = new ArrayList<>();
        Field[] fields = dataClass.getDeclaredFields();

        int columIndex = 0;
        for(Field f : fields){
            if(f.isAnnotationPresent(ExportColumn.class)){
                ExportColumn exportColumn = f.getAnnotation(ExportColumn.class);
                cellConfigList.add(new CellConfig(columIndex++,exportColumn.name(),f.getName()));
            }
        }
        exportConfig.setCellExportConfigList(cellConfigList);
        return exportConfig;
    }

    public static final ExportConfig transactionExport = createExportConfig(TransactionDto.class, 0, 1);
    public static final ExportConfig productsExport = createExportConfig(ProductsReportDto.class, 0, 1);
    public static final ExportConfig productsReportExport = createExportConfig(ProductsReportDto.class, 0, 1);
    public static final ExportConfig warehouseExport = createExportConfig(WarehousesDto.class, 0, 1);
    public static final ExportConfig expensesExport = createExportConfig(ExpensesDto.class, 0, 1);
    public static final ExportConfig expensesConfigExport = createExportConfig(ExpensesConfigDto.class, 0, 1);
    public static final ExportConfig summarySalaryExport = createExportConfig(SummarySalaryDto.class, 0, 1);
    public static final ExportConfig scheduleConfigExport = createExportConfig(ScheduleConfigEntity.class, 0, 1);
    public static final ExportConfig scheduleExport = createExportConfig(ScheduleDto.class, 0, 1);
    public static final ExportConfig truckExport = createExportConfig(TruckDto.class, 0, 1);
    public static final ExportConfig userExport = createExportConfig(UserDto.class, 0, 1);
    public static final ExportConfig expenseReportByDriverExport = createExportConfig(ExpensesIncurredDto.class, 0, 1);
}