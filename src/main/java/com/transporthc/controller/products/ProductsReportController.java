package com.transporthc.controller.products;

import com.transporthc.dto.BaseResponse;
import com.transporthc.dto.products.ProductsReportDto;
import com.transporthc.dto.ExportExcelResponse;
import com.transporthc.service.products.ProductsReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/products-report")
@RequiredArgsConstructor
public class ProductsReportController {

    private final ProductsReportService productsReportService;

    @PostMapping("/create")
    public void creatProductsReport(@RequestParam String period) {
        productsReportService.createProductsReport(period);
    }

    @GetMapping("/by-month")
    public ResponseEntity<Object> getProductsReport(@RequestParam String period) {
        List<ProductsReportDto> productsReports = productsReportService.getProductsReport(period);
        return ResponseEntity.ok(BaseResponse.ok(productsReports));
    }

    @GetMapping("/export")
    public ResponseEntity<Object> exportProductsReport(@RequestParam String period) throws Exception {
        ExportExcelResponse exportExcelResponse = productsReportService.exportProductsReport(period);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=" + URLEncoder.encode(exportExcelResponse.getFilename(), StandardCharsets.UTF_8)
                )
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel; charset=UTF-8"))
                .body(exportExcelResponse.getResource());
    }
}