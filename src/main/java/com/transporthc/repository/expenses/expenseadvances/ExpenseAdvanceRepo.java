package com.transporthc.repository.expenses.expenseadvances;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.transporthc.entity.expenses.ExpenseAdvances;

@Repository
public interface ExpenseAdvanceRepo extends JpaRepository<ExpenseAdvances,Integer>,ExpenseAdvancesRepoCustom {
    
}
