package com.transporthc.repository.expenses.expensesconfig;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.transporthc.entity.expenses.ExpenseConfigEntity;

@Repository
public interface ExpensesConfigRepo extends JpaRepository<ExpenseConfigEntity,String>,ExpensesConfigRepoCustom {
    Optional<ExpenseConfigEntity> findById(String id);
}
