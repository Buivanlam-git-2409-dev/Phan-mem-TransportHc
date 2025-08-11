package com.transporthc.repository.expenses.expenses;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import static com.transporthc.entity.attached.QAttachedImgEntity.attachedImgEntity;
import static com.transporthc.entity.expenses.QExpensesEntity.expensesEntity;
import static com.transporthc.entity.expenses.QExpensesConfigEntity.expensesConfigEntity;
import static com.transporthc.entity.schedule.QScheduleEntity.scheduleEntity;
import static com.transporthc.entity.truck.QTruckEntity.truckEntity;
import static com.transporthc.entity.user.QUserEntity.userEntity;

import java.time.YearMonth;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.transporthc.entity.expenses.QExpenseAdvancesEntity.expenseAdvancesEntity;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.transporthc.dto.expenses.ExpensesDto;
import com.transporthc.dto.expenses.ExpensesIncurredDto;
import com.transporthc.dto.expenses.ExpensesReportDto;
import com.transporthc.enums.Pagination;
import com.transporthc.enums.expenses.ExpensesStatusEnum;
import com.transporthc.enums.role.UserRoleEnum;
import com.transporthc.repository.BaseRepo;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@Repository
public class ExpensesRepoImpl extends BaseRepo implements ExpensesRepoCustom{
    public ExpensesRepoImpl(EntityManager entityManager){
        super(entityManager);
    }
    
    private ConstructorExpression<ExpensesDto> expensesProjection(){
        return Projections.constructor(ExpensesDto.class,
                expensesEntity.id.as("id"),
                userEntity.id.as("driverId"),
                userEntity.fullName.as("driverName"),
                expensesEntity.expensesConfigIdEntity.as("expensesConfigId"),
                JPAExpressions.select(expensesConfigEntity.type.as("expensesConfigType"))
                        .from(expensesConfigEntity)
                        .where(expensesConfigEntity.id.eq(expensesEntity.expensesConfigId)),
                expensesEntity.amount.as("amount"),
                expensesEntity.note.coalesce("").as("note"),
                JPAExpressions.select(
                                Expressions.stringTemplate("GROUP_CONCAT({0})", attachedImgEntity.imgPath).as("attachedPaths"))
                        .from(attachedImgEntity)
                        .where(attachedImgEntity.referenceId.eq(expensesEntity.id)),
                expensesEntity.scheduleId.as("scheduleId"),
                expensesEntity.status.as("status"),
                expensesEntity.createdAt.as("createdAt"),
                expensesEntity.updatedAt.as("updatedAt")
        );
    }
    BooleanBuilder initGetAllBuilder(String configId, String truckLicense, Date fromDate, Date toDate) {
        BooleanBuilder builder = new BooleanBuilder().and(expensesEntity.deleted.eq(false));
        if (configId != null) {
            builder.and(expensesEntity.expensesConfigId.eq(configId));
        }
        if (truckLicense != null && !truckLicense.isBlank()) {
            builder.and(scheduleEntity.truckLicense.eq(truckLicense));
        }
        if (fromDate != null && toDate != null) {
            builder.and(expensesEntity.createdAt.between(fromDate, toDate));
        } else if (fromDate != null) {
            builder.and(expensesEntity.createdAt.goe(fromDate));
        } else if (toDate != null) {
            builder.and(expensesEntity.createdAt.loe(toDate));
        }
        return builder;
    }
    BooleanBuilder initGetOneBuilder(String id) {
        return new BooleanBuilder()
                .and(expensesEntity.deleted.eq(false))
                .and(expensesEntity.id.eq(id));
    }
    @Override
    public List<ExpensesDto> getAll(int page, String expensesConfigId, String truckLicense, Date fromDate, Date toDate) {
        BooleanBuilder builder = initGetAllBuilder(expensesConfigId, truckLicense, fromDate, toDate);
        long offset = (long) (page - 1) * Pagination.TEN.getSize();
        return query.from(expensesEntity)
                .innerJoin(scheduleEntity).on(expensesEntity.scheduleId.eq(scheduleEntity.id))
                .innerJoin(truckEntity).on(scheduleEntity.truckLicense.eq(truckEntity.licensePlate))
                .innerJoin(userEntity).on(truckEntity.driverId.eq(userEntity.id))
                .where(builder)
                .select(expensesProjection())
                .offset(offset)
                .limit(Pagination.TEN.getSize())
                .fetch();
    }
    @Override
    public List<ExpensesIncurredDto> getExpenseIncurredByDriverID(String driverId, Date fromDate, Date toDate) {
        BooleanBuilder builder = new BooleanBuilder()
                .and(expensesEntity.deleted.eq(false));

        if (driverId != null && !driverId.isBlank()) {
            builder.and(truckEntity.driverId.eq(driverId));
        }
        if (fromDate != null && toDate != null) {
            builder.and(expensesEntity.createdAt.between(fromDate, toDate));
        }

        ConstructorExpression<ExpensesIncurredDto> expensesIncurredExpression = Projections.constructor(
                ExpensesIncurredDto.class,
                expensesEntity.expensesConfigId.as("expensesConfigId"),
                expensesConfigEntity.type.as("type"),
                expensesEntity.amount.sum().as("amount")
        );
        return query.from(expensesEntity)
                .innerJoin(expensesConfigEntity).on(expensesEntity.expensesConfigId.eq(expensesConfigEntity.id))
                .innerJoin(scheduleEntity).on(expensesEntity.scheduleId.eq(scheduleEntity.id))
                .innerJoin(truckEntity).on(scheduleEntity.truckLicense.eq(truckEntity.licensePlate))
                .where(builder)
                .select(expensesIncurredExpression)
                .groupBy(expensesEntity.expensesConfigId, expensesConfigEntity.type)
                .fetch();
    }
    @Override
    public Optional<ExpensesDto> getByID(String id) {
        BooleanBuilder builder = initGetOneBuilder(id);
        return Optional.ofNullable(
                query.from(expensesEntity)
                        .innerJoin(scheduleEntity).on(expensesEntity.scheduleId.eq(scheduleEntity.id))
                        .innerJoin(truckEntity).on(scheduleEntity.truckLicense.eq(truckEntity.licensePlate))
                        .innerJoin(userEntity).on(truckEntity.driverId.eq(userEntity.id))
                        .where(builder)
                        .select(expensesProjection())
                        .fetchOne()
        );
    }
    @Override
    @Modifying
    @Transactional
    public long delete(String id) {
        BooleanBuilder builder = initGetOneBuilder(id);
        return query.update(expensesEntity)
                .where(builder)
                .set(expensesEntity.deleted, true)
                .execute();
    }

    @Override
    @Modifying
    @Transactional
    public long approve(String id) {
        BooleanBuilder builder = initGetOneBuilder(id)
                .and(expensesEntity.status.eq(ExpensesStatusEnum.PENDING.getValue()));
        return query.update(expensesEntity)
                .where(builder)
                .set(expensesEntity.status, ExpensesStatusEnum.APPROVED.getValue())
                .execute();
    }

    //
     @Override
    public List<ExpensesReportDto> reportAll(String period) {
        ConstructorExpression<ExpensesIncurredDto> expensesIncurredExpression = Projections.constructor(
                ExpensesIncurredDto.class,
                expensesConfigEntity.id.as("expensesConfigId"),
                expensesConfigEntity.type.as("type"),
                expensesEntity.amount.sum().as("amount")
        );

        List<ExpensesReportDto> reports = reportsQuery(period);

        for (ExpensesReportDto report : reports) {
            report.setExpensesIncurred(
                    expensesIncurredEachDriverQuery(report.getDriverId(), expensesIncurredExpression)
            );
        }

        return reports;
    }

    @Override
    public long countByID(String id) {
        BooleanBuilder builder = initGetOneBuilder(id);
        Long res = query.from(expensesEntity)
                .where(builder)
                .select(expensesEntity.id.count().coalesce(0L))
                .fetchOne();
        return res != null ? res : 0;
    }

    @Override
    public ExpensesStatusEnum getStatusByID(String id) {
        BooleanBuilder builder = initGetOneBuilder(id);
        Integer statusNumber = query.from(expensesEntity)
                .where(builder)
                .select(expensesEntity.status)
                .fetchOne();
        return statusNumber != null ? ExpensesStatusEnum.valueOf(statusNumber) : null;
    }

    private String prevPeriod(String period) {
        YearMonth yearMonth = YearMonth.parse(period);
        YearMonth prevMonth = yearMonth.minusMonths(1);
        return prevMonth.toString();
    }

    private List<ExpensesReportDto> reportsQuery(String period) {
        String prevPeriod = prevPeriod(period);

        ConstructorExpression<ExpensesReportDto> expression = Projections.constructor(
                ExpensesReportDto.class,
                userEntity.id.as("driverId"),
                userEntity.fullName.as("driverName"),
                Expressions.stringTemplate("GROUP_CONCAT(DISTINCT {0})", scheduleEntity.truckLicense).as("truckLicense"),
                Expressions.stringTemplate("GROUP_CONCAT(DISTINCT {0})", scheduleEntity.moocLicense).as("moocLicense"),
                JPAExpressions.select(expenseAdvancesEntity.remainingBalance.coalesce(0f).as("prevRemainingBalance"))
                        .from(expenseAdvancesEntity)
                        .where(expenseAdvancesEntity.driverId.eq(userEntity.id).and(expenseAdvancesEntity.period.eq(prevPeriod))),
                expenseAdvancesEntity.advance.sum().coalesce(0f).as("advance"),
                expenseAdvancesEntity.remainingBalance.sum().coalesce(0f).as("remainingBalance")
        );

        BooleanBuilder builder = new BooleanBuilder()
                .and(userEntity.roleId.eq(UserRoleEnum.DRIVER.getId()))
                .and(expenseAdvancesEntity.period.eq(period));

        return query.from(userEntity)
                .leftJoin(truckEntity).on(truckEntity.driverId.eq(user.id))
                .leftJoin(scheduleEntity).on(truckEntity.licensePlate.eq(scheduleEntity.truckLicense))
                .leftJoin(expenseAdvancesEntity).on(expenseAdvancesEntity.driverId.eq(userEntity.id))
                .where(builder)
                .select(expression)
                .groupBy(userEntity.id, userEntity.fullName)
                .fetch();
    }

    private List<ExpensesIncurredDto> expensesIncurredEachDriverQuery(String driverId, ConstructorExpression<ExpensesIncurredDto> expression) {
        return query.from(expensesEntity)
                .innerJoin(expensesConfigEntity).on(expensesEntity.expensesConfigIdEntity.eq(expensesConfigEntity.id))
                .innerJoin(scheduleEntity).on(expensesEntity.scheduleId.eq(scheduleEntity.id))
                .innerJoin(truckEntity).on(scheduleEntity.truckLicense.eq(truckEntity.licensePlate))
                .select(expression)
                .where(truckEntity.driverId.eq(driverId))
                .groupBy(expensesConfigEntity.id, expensesConfigEntity.type)
                .fetch();
    }
}