package com.transporthc.service.transaction;

import com.transporthc.dto.ExportExcelResponse;
import com.transporthc.dto.transaction.TransactionDto;
import com.transporthc.entity.transaction.Transaction;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public interface TransactionService {
    TransactionDto createTransaction(TransactionDto transactionDto);
    Optional<TransactionDto> updateTransaction(String id, TransactionDto dto);
    String deleteTransaction(String id);
    TransactionDto getTransactionById(String id);
    List<TransactionDto> getTransactionByFilter(int page, String warehouseId, Boolean origin, String fromDateStr, String toDateStr);
    List<Transaction> importTransactionData(MultipartFile importFile);
    ExportExcelResponse exportTransaction(int page, String warehouseId, Boolean origin, String fromDate, String toDate) throws Exception;
}