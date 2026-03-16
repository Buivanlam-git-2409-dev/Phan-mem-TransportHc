package com.transporthc.repository.expenses.expenses;

import com.transporthc.entity.expenses.Expenses;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpensesRepo extends JpaRepository<Expenses, String>, ExpensesRepoCustom {
    @Override
    Optional<Expenses> findById(@NonNull String id);
}