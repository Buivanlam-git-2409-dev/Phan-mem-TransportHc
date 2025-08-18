package com.transporthc.controller.expenses;

import com.transporthc.dto.ExportExcelResponse;
import com.transporthc.dto.expenses.ExpensesConfigDto;
import com.transporthc.dto.BaseResponse;
import com.transporthc.service.expenses.expensesConfig.ExpensesConfigService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/expenses/configs")
@RequiredArgsConstructor
public class ExpensesConfigController {
    private final ExpensesConfigService expensesConfigService;

    @GetMapping()
    public ResponseEntity<Object> getExpensesConfigs() {
        return ResponseEntity.ok(
                BaseResponse.ok(expensesConfigService.getAll())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getExpensesConfigByID(@PathVariable String id) {
        return ResponseEntity.ok(
                BaseResponse.ok(expensesConfigService.getByID(id))
        );
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createExpensesConfig(@Valid @RequestBody ExpensesConfigDto dto) {
        return new ResponseEntity<>(
                BaseResponse.ok(expensesConfigService.create(dto)),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<Object> updateExpensesConfig(@PathVariable String id, @RequestBody ExpensesConfigDto dto) {
        return ResponseEntity.ok(
                BaseResponse.ok(expensesConfigService.update(id, dto))
        );
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<Object> deleteExpensesConfigByID(@PathVariable String id) {
        long res = expensesConfigService.deleteByID(id);
        return ResponseEntity.ok(
                BaseResponse.ok(res, "Đã xóa thành công " + res + " cấu hình chi phí")
        );
    }

    @GetMapping("/export")
    public ResponseEntity<Object> exportExpensesConfig() throws Exception {
        ExportExcelResponse exportExcelResponse = expensesConfigService.exportExpensesConfig();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=" + URLEncoder.encode(exportExcelResponse.getFilename(), StandardCharsets.UTF_8)
                )
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel; charset=UTF-8"))
                .body(exportExcelResponse.getResource());
    }

    @PostMapping("/import")
    public ResponseEntity<Object> importExpensesConfigData(@RequestParam("file") MultipartFile importFile) {
        return new ResponseEntity<>(
                BaseResponse.ok(expensesConfigService.importExpensesConfigData(importFile)),
                HttpStatus.CREATED
        );
    }
}