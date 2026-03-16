package com.transporthc.repository.products.productsReport;

import java.util.Date;
import java.util.List;

import com.transporthc.dto.products.ProductsReportDto;
import com.transporthc.entity.products.ProductsReport;

public interface ProductsReportRepoCustom {
    ProductsReport getProductReport(String productsId, Date fromDate,Date toDate);
    List<ProductsReportDto> getProductReportDto(Date fromDate, Date toDate);
}
