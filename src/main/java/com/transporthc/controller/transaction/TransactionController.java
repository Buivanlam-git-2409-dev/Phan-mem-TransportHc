package com.transporthc.controller.transaction;

import com.transporthc.dto.BaseResponse;
import com.transporthc.dto.ExportExcelResponse;
import com.transporthc.dto.transaction.TransactionDto;
import com.transporthc.service.transaction.TransactionService;
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
import java.util.List;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/create")
    public ResponseEntity<Object> createTransaction(@Valid @RequestBody TransactionDto dto) {
        return new ResponseEntity<>(
                BaseResponse.ok(transactionService.createTransaction(dto)),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<Object> updateTransaction(@PathVariable String id, @RequestBody TransactionDto dto) {
        return ResponseEntity.ok(BaseResponse.ok(transactionService.updateTransaction(id, dto)));
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<Object> deleteTransaction(@PathVariable String id) {
        return ResponseEntity.ok(BaseResponse.ok(transactionService.deleteTransaction(id)));
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<Object> getTransactionById(@PathVariable String id) {
        return ResponseEntity.ok(BaseResponse.ok(transactionService.getTransactionById(id)));
    }

    @GetMapping("/filter")
    public ResponseEntity<Object> getTransactionByFilter(
            @RequestParam(required = false) int page,
            @RequestParam(required = false) String warehouseId,
            @RequestParam(required = false) Boolean origin,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate) {

        List<TransactionDto> transactions = transactionService.getTransactionByFilter(page, warehouseId, origin, fromDate, toDate);
        return ResponseEntity.ok(BaseResponse.ok(transactions));
    }

    @GetMapping("/export")
    public ResponseEntity<Object> exportTransaction(
            @RequestParam int page,
            @RequestParam(required = false) String warehouseId,
            @RequestParam(required = false) Boolean origin,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate) throws Exception {

        ExportExcelResponse exportExcelResponse = transactionService.exportTransaction(page, warehouseId, origin, fromDate, toDate);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=" + URLEncoder.encode(exportExcelResponse.getFilename(), StandardCharsets.UTF_8)
                )
                .contentType(MediaType.parseMediaType("application/vnd.ms-excel; charset=UTF-8"))
                .body(exportExcelResponse.getResource());
    }

    @PostMapping("/import")
    public ResponseEntity<Object> importTransactionData(@RequestParam("file") MultipartFile importFile) {
        return new ResponseEntity<>(
                BaseResponse.ok(transactionService.importTransactionData(importFile)),
                HttpStatus.CREATED
        );
    }
}