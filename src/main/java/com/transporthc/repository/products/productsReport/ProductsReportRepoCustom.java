package com.transporthc.repository.products.productsReport;

import java.util.Date;
import java.util.List;

import com.transporthc.dto.products.ProductsDto;
import com.transporthc.entity.products.ProductsReportEntity;

public interface ProductsReportRepoCustom {
    ProductsReportEntity getProductReport(String productsId, Date fromDate,Date toDate);
    List<ProductsDto> getProductReportDto(Date fromDate, Date toDate);
}
