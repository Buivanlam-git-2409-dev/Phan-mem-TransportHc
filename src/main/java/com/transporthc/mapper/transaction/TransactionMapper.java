package com.transporthc.mapper.transaction;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.transporthc.dto.transaction.TransactionDto;
import com.transporthc.entity.transaction.Transaction;
import com.transporthc.enums.IDKey;
import com.transporthc.utils.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TransactionMapper {
    public Transaction toTransaction(TransactionDto dto) {
        if (dto == null) return null;
        return Transaction.builder()
                .id(utils.genID(IDKey.TRANSACTION))
                .refUserId(dto.getRefUserId())
                .customerName(dto.getCustomerName())
                .productsId(dto.getGoodsId())
                .quantity(dto.getQuantity())
                .transactionTime(dto.getTransactionTime())
                .origin(dto.getOrigin().getValue())
                .destination(dto.getDestination())
                .image(dto.getImage())
                .build();
    }

    public List<Transaction> toTransactions(List<TransactionDto> dtos) {
        if(dtos == null || dtos.isEmpty()) {
            return Collections.emptyList();
        }
        return dtos.stream().map(this::toTransaction).collect(Collectors.toList());
    }
}
