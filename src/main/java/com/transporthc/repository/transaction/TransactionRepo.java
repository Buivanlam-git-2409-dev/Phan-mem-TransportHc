package com.transporthc.repository.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.transporthc.entity.transaction.TransactionEntity;

@Repository
public interface TransactionRepo extends JpaRepository<TransactionEntity, String>, TransactionRepoCustom {
}