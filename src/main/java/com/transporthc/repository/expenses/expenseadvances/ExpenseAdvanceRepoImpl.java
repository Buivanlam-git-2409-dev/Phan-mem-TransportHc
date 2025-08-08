package com.transporthc.repository.expenses.expenseadvances;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.transporthc.dto.expenses.ExpensesAdvancesDto;
import com.transporthc.enums.Pagination;
import com.transporthc.repository.BaseRepo;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

public class ExpenseAdvanceRepoImpl extends BaseRepo implements ExpenseAdvancesRepoCustom{
    public ExpenseAdvanceRepoImpl(EntityManager entityManager){
        super(entityManager);
    }

    private ConstructorExpression<ExpensesAdvancesDto> constructorExpression() {
        return Projections.constructor(ExpensesAdvancesDto.class,
                expenseAdvances.id.as("id"),
                expenseAdvances.driverId.as("driverId"),
                JPAExpressions.select(user.fullName.as("driverName"))
                        .from(user)
                        .where(expenseAdvances.driverId.eq(user.id)),
                expenseAdvances.period.as("period"),
                expenseAdvances.advance.as("advance"),
                expenseAdvances.remainingBalance.coalesce(0f).as("remainingBalance"),
                expenseAdvances.note.coalesce("").as("note"),
                expenseAdvances.createdAt.as("createdAt"),
                expenseAdvances.updatedAt.as("updatedAt")
        );
    }
    @Override
    public List<ExpensesAdvancesDto> getAll(int page) {
        BooleanBuilder builder = new BooleanBuilder()
                .and(expenseAdvances.deleted.eq(false));

        long offset = (long) (page - 1) * Pagination.TEN.getSize();
        return query.from(expenseAdvances)
                .where(builder)
                .select(constructorExpression())
                .offset(offset)
                .limit(Pagination.TEN.getSize())
                .fetch();
    }
    @Override
    public Optional<ExpensesAdvancesDto> getByDriverId(String id) {
        BooleanBuilder builder = new BooleanBuilder()
                .and(expenseAdvances.deleted.eq(false))
                .and(expenseAdvances.driverId.eq(id));

        return Optional.ofNullable(
                query.from(expenseAdvances)
                        .where(builder)
                        .select(constructorExpression())
                        .fetchOne()
        );
    }
    BooleanBuilder initBuilder(Integer id) {
        return new BooleanBuilder()
                .and(expenseAdvances.deleted.eq(false))
                .and(expenseAdvances.id.eq(id));
    }

    @Override
    @Modifying
    @Transactional
    public long deleted(Integer id) {
        BooleanBuilder builder = initBuilder(id);
        return query.update(expenseAdvances)
                .where(builder)
                .set(expenseAdvances.deleted, true)
                .execute();
    }

    @Override
    public Optional<ExpensesAdvancesDto> getExpenseAdvanceById(Integer id) {
        BooleanBuilder builder = initBuilder(id);
        return Optional.ofNullable(
                query.from(expenseAdvances)
                        .where(builder)
                        .select(constructorExpression())
                        .fetchOne()
        );
    }
}
