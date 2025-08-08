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

import static com.transporthc.entity.expenses.QExpenseAdvancesEntity.expenseAdvancesEntity;
import static com.transporthc.entity.user.QUserEntity.userEntity;
public class ExpenseAdvanceRepoImpl extends BaseRepo implements ExpenseAdvancesRepoCustom{
    public ExpenseAdvanceRepoImpl(EntityManager entityManager){
        super(entityManager);
    }

    private ConstructorExpression<ExpensesAdvancesDto> constructorExpression() {
        return Projections.constructor(ExpensesAdvancesDto.class,
                expenseAdvancesEntity.id.as("id"),
                expenseAdvancesEntity.driverId.as("driverId"),
                JPAExpressions.select(userEntity.fullName.as("driverName"))
                        .from(userEntity)
                        .where(expenseAdvancesEntity.driverId.eq(userEntity.id)),
                expenseAdvancesEntity.period.as("period"),
                expenseAdvancesEntity.advance.as("advance"),
                expenseAdvancesEntity.remainingBalance.coalesce(0f).as("remainingBalance"),
                expenseAdvancesEntity.note.coalesce("").as("note"),
                expenseAdvancesEntity.createdAt.as("createdAt"),
                expenseAdvancesEntity.updatedAt.as("updatedAt")
        );
    }
    @Override
    public List<ExpensesAdvancesDto> getAll(int page) {
        BooleanBuilder builder = new BooleanBuilder()
                .and(expenseAdvancesEntity.deleted.eq(false));

        long offset = (long) (page - 1) * Pagination.TEN.getSize();
        return query.from(expenseAdvancesEntity)
                .where(builder)
                .select(constructorExpression())
                .offset(offset)
                .limit(Pagination.TEN.getSize())
                .fetch();
    }
    @Override
    public Optional<ExpensesAdvancesDto> getByDriverId(String id) {
        BooleanBuilder builder = new BooleanBuilder()
                .and(expenseAdvancesEntity.deleted.eq(false))
                .and(expenseAdvancesEntity.driverId.eq(id));

        return Optional.ofNullable(
                query.from(expenseAdvancesEntity)
                        .where(builder)
                        .select(constructorExpression())
                        .fetchOne()
        );
    }
    BooleanBuilder initBuilder(Integer id) {
        return new BooleanBuilder()
                .and(expenseAdvancesEntity.deleted.eq(false))
                .and(expenseAdvancesEntity.id.eq(id));
    }

    @Override
    @Modifying
    @Transactional
    public long deleted(Integer id) {
        BooleanBuilder builder = initBuilder(id);
        return query.update(expenseAdvancesEntity)
                .where(builder)
                .set(expenseAdvancesEntity.deleted, true)
                .execute();
    }

    @Override
    public Optional<ExpensesAdvancesDto> getExpenseAdvanceById(Integer id) {
        BooleanBuilder builder = initBuilder(id);
        return Optional.ofNullable(
                query.from(expenseAdvancesEntity)
                        .where(builder)
                        .select(constructorExpression())
                        .fetchOne()
        );
    }
}
