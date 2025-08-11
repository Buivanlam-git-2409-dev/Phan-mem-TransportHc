package com.transporthc.repository.expenses.expensesconfig;


import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.transporthc.dto.expenses.ExpensesConfigDto;
import com.transporthc.repository.BaseRepo;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.transporthc.entity.expenses.QExpensesConfigEntity.expensesConfigEntity;

@Repository
public class ExpensesConfigRepoImpl extends BaseRepo implements ExpensesConfigRepoCustom {
    public ExpensesConfigRepoImpl(EntityManager entityManager) {
        super(entityManager);
    }

    Expression<ExpensesConfigDto> expression = Projections.fields(
            ExpensesConfigDto.class,
            expensesConfigEntity.id.as("id"),
            expensesConfigEntity.type.as("type"),
            expensesConfigEntity.note.as("note"),
            expensesConfigEntity.createdAt.as("createdAt"),
            expensesConfigEntity.updatedAt.as("updatedAt")
    );

    @Override
    public List<ExpensesConfigDto> getAll() {
        return query.from(expensesConfigEntity)
                .where(expensesConfigEntity.deleted.eq(false))
                .select(expression)
                .fetch();
    }

    BooleanBuilder initBuilder(String id) {
        return new BooleanBuilder()
                .and(expensesConfigEntity.id.eq(id))
                .and(expensesConfigEntity.deleted.eq(false));
    }

    @Override
    public Optional<ExpensesConfigDto> getByID(String id) {
        BooleanBuilder builder = initBuilder(id);
        return Optional.ofNullable(
                query.from(expensesConfigEntity)
                        .where(builder)
                        .select(expression)
                        .fetchOne()
        );
    }

    @Override
    @Modifying
    @Transactional
    public long delete(String id) {
        BooleanBuilder builder = initBuilder(id);
        return query.update(expensesConfigEntity)
                .where(builder)
                .set(expensesConfigEntity.deleted, true)
                .execute();
    }
}