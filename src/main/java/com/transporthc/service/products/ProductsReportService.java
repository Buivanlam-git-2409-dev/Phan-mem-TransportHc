package com.transporthc.service.products;

import java.util.List;

import org.springframework.stereotype.Service;
import com.transporthc.dto.products.ProductsReportDto;
import com.transporthc.dto.ExportExcelResponse;

@Service
public interface ProductsReportService {
    void createProductsReport(String period);
    List<ProductsReportDto> getProductsReport(String period);
    ExportExcelResponse exportProductsReport(String period) throws Exception;
}
