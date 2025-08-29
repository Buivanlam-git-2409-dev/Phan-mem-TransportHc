package com.transporthc.repository.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.transporthc.entity.transaction.Transaction;

@Repository
public interface TransactionRepo extends JpaRepository<Transaction, String>, TransactionRepoCustom {
}