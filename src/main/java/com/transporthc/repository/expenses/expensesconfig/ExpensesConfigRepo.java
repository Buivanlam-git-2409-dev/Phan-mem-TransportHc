package com.transporthc.repository.expenses.expensesconfig;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.transporthc.entity.expenses.ExpenseConfig;

@Repository
public interface ExpensesConfigRepo extends JpaRepository<ExpenseConfig,String>,ExpensesConfigRepoCustom {
    Optional<ExpenseConfig> findById(String id);
}
