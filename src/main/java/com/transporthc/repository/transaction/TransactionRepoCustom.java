package com.transporthc.repository.transaction;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.transporthc.dto.transaction.TransactionDto;
import com.transporthc.entity.transaction.Transaction;

public interface TransactionRepoCustom {
    Optional<TransactionDto> getTransactionsById(String id);
    List<TransactionDto> getTransactionByFilter(int page, String warehouseId, Boolean origin, Date fromDate, Date toDate);
    long deleteTransaction(String id);
    long updateTransaction(Transaction transaction, String id, TransactionDto dto);
    Float getQuantityByOrigin(String goodsId, Boolean origin , Date fromDate, Date toDate);
}
