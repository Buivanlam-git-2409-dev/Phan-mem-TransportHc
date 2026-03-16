package com.transporthc.controller.products;

import com.transporthc.dto.BaseResponse;
import com.transporthc.dto.ExportExcelResponse;
import com.transporthc.service.products.ProductsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductsController {
    private final ProductsService productsService;

    @GetMapping("/filter")
    public ResponseEntity<Object> getProductsByFilter(@RequestParam(required = false) String warehouseId) {
        return ResponseEntity.ok(
                BaseResponse.ok(productsService.getProductsByFilter(warehouseId))
        );
    }

    @GetMapping("/export")
    public ResponseEntity<Object> exportProducts(@RequestParam(required = false) String warehouseId) throws Exception {

        ExportExcelResponse exportExcelResponse = productsService.exportProducts(warehouseId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=" + URLEncoder.encode(exportExcelResponse.getFilename(), StandardCharsets.UTF_8)
                )
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel; charset=UTF-8"))
                .body(exportExcelResponse.getResource());
    }
}