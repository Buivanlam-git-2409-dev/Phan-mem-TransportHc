package com.transporthc.controller.warehouses;

import com.transporthc.dto.BaseResponse;
import com.transporthc.dto.ExportExcelResponse;
import com.transporthc.service.warehouses.WarehousesService;
import lombok.RequiredArgsConstructor;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/warehouse")
@RequiredArgsConstructor
public class WarehousesController {
    private final WarehousesService warehousesService;

    @GetMapping("/getAll")
    public ResponseEntity<Object> getAllWarehouse() {
        return ResponseEntity.ok(
            BaseResponse.ok(warehousesService.getAllWarehouses())
        );
    }
    
    @GetMapping("/export")
    public ResponseEntity<Object> exportWarehouses() throws Exception{
        ExportExcelResponse eer = warehousesService.exportWarehouses();
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
        "attached; filename = "+URLEncoder.encode(eer.getFilename(), StandardCharsets.UTF_8))
        .contentType(MediaType.parseMediaType("application/vnd.ms-excel; charset=UTF-8"))
        .body(eer.getResource());
    }
}
