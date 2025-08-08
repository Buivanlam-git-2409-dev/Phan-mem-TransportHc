package com.transporthc.repository.expenses.expenses;

import com.transporthc.entity.expenses.ExpensesEntity;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpensesRepo extends JpaRepository<ExpensesEntity, String>, ExpensesRepoCustom {
    @Override
    Optional<ExpensesEntity> findById(@NonNull String id);
}