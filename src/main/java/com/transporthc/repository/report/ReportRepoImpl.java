package com.transporthc.repository.report;


import com.transporthc.dto.report.ReportDetailSalaryDto;
import com.transporthc.dto.report.SummarySalaryDto;
import com.transporthc.dto.salary.SalaryDto;
import com.transporthc.dto.schedule.ScheduleSalaryDto;
import com.transporthc.entity.*;
import com.transporthc.enums.schedule.ScheduleTypeEnum;
import com.transporthc.repository.BaseRepo;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import jakarta.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import static com.transporthc.entity.user.QUserEntity.userEntity;
import static com.transporthc.entity.truck.QTruckEntity.truckEntity;
import static com.transporthc.entity.schedule.QScheduleEntity.scheduleEntity;
import static com.transporthc.entity.schedule.QScheduleConfigEntity.scheduleConfigEntity;
import static com.transporthc.entity.salary.QSalaryEntity.salaryEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class ReportRepoImpl extends BaseRepo implements ReportRepo {
    public ReportRepoImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public ReportDetailSalaryDto getReport(String userId, String period, Date fromDate, Date toDate) {
        NumberExpression<Float> minAmount = scheduleConfigEntity.amount.min().coalesce(0F).as("minAmount");
        NumberExpression<Long> scheduleCount = scheduleEntity.id.count().coalesce(0L).as("scheduleCount");
        NumberExpression<Float> total = scheduleConfigEntity.amount.min().multiply(scheduleEntity.id.count()).coalesce(0F).as("total");

        List<Tuple> results = query
                .select(
                        userEntity.fullName,
                        scheduleConfigEntity.placeA,
                        scheduleConfigEntity.placeB,
                        minAmount,
                        scheduleCount,
                        total,
                        salaryEntity.phoneAllowance,
                        salaryEntity.basicSalary,
                        salaryEntity.jobAllowance,
                        salaryEntity.bonus,
                        salaryEntity.monthlyPaidLeave,
                        salaryEntity.ot,
                        salaryEntity.receivedSnn,
                        salaryEntity.unionContribution,
                        salaryEntity.travelExpensesReimbursement,
                        salaryEntity.mandatoryInsurance,
                        salaryEntity.tradeUnion,
                        salaryEntity.advance,
                        salaryEntity.errorOfDriver,
                        salaryEntity.deductionSnn
                )
                .from(userEntity)
                .join(truckEntity).on(userEntity.id.eq(truckEntity.driverId))
                .join(scheduleEntity).on(truckEntity.licensePlate.eq(scheduleEntity.truckLicense))
                .join(scheduleConfigEntity).on(scheduleEntity.scheduleConfigId.eq(scheduleConfigEntity.id))
                .leftJoin(salaryEntity).on(userEntity.id.eq(salaryEntity.userId).and(salaryEntity.period.eq(period)))
                .where(
                        userEntity.id.eq(userId)
                                .and(scheduleEntity.departureTime.between(fromDate, toDate))
                )
                .groupBy(
                        userEntity.fullName,
                        scheduleConfigEntity.placeA, scheduleConfigEntity.placeB,
                        salaryEntity.phoneAllowance, salaryEntity.basicsalaryEntity, salaryEntity.jobAllowance, salaryEntity.bonus,
                        salaryEntity.monthlyPaidLeave, salaryEntity.ot, salaryEntity.receivedSnn, salaryEntity.unionContribution,
                        salaryEntity.travelExpensesReimbursement,
                        salaryEntity.mandatoryInsurance, salaryEntity.tradeUnion, salaryEntity.advance,
                        salaryEntity.errorOfDriver, salaryEntity.deductionSnn
                )
                .fetch();

        ReportDetailSalaryDto report = new ReportDetailSalaryDto();
        SalaryDto salaryDTO = null;
        List<ScheduleSalaryDto> schedules = new ArrayList<>();

        if (!results.isEmpty()) {
            for (Tuple tuple : results) {
                if (salaryDTO == null) {
                    salaryDTO = SalaryDTO.builder()
                            .phoneAllowance(coalesce(tuple.get(salaryEntity.phoneAllowance), 0F))
                            .basicSalary(coalesce(tuple.get(salaryEntity.basicSalary), 0F))
                            .jobAllowance(coalesce(tuple.get(salaryEntity.jobAllowance), 0F))
                            .bonus(coalesce(tuple.get(salaryEntity.bonus), 0F))
                            .monthlyPaidLeave(coalesce(tuple.get(salaryEntity.monthlyPaidLeave), 0F))
                            .ot(coalesce(tuple.get(salaryEntity.ot), 0F))
                            .receivedSnn(coalesce(tuple.get(salaryEntity.receivedSnn), 0F))
                            .unionContribution(coalesce(tuple.get(salaryEntity.unionContribution), 0F))
                            .travelExpensesReimbursement(coalesce(tuple.get(salaryEntity.travelExpensesReimbursement), 0F))
                            .mandatoryInsurance(coalesce(tuple.get(salaryEntity.mandatoryInsurance), 0F))
                            .tradeUnion(coalesce(tuple.get(salaryEntity.tradeUnion), 0F))
                            .advance(coalesce(tuple.get(salaryEntity.advance), 0F))
                            .errorOfDriver(coalesce(tuple.get(salaryEntity.errorOfDriver), 0F))
                            .deductionSnn(coalesce(tuple.get(salaryEntity.deductionSnn), 0F))
                            .build();
                }

                ScheduleSalaryDto schedule = new ScheduleSalaryDto(
                        tuple.get(userEntity.fullName),
                        tuple.get(scheduleConfigEntity.placeA),
                        tuple.get(scheduleConfigEntity.placeB),
                        coalesce(tuple.get(minAmount), 0F),
                        coalesce(tuple.get(scheduleCount), 0L).intValue(),
                        coalesce(tuple.get(total), 0F)
                );
                schedules.add(schedule);
            }
        } else {
            salaryDTO = SalaryDTO.builder().build();
        }

        report.setSalary(salaryDTO);
        report.setSchedules(schedules);

        return report;
    }

    /**
     * Phương thức hỗ trợ xử lý COALESCE
     * Trả về giá trị nếu không null, ngược lại trả về giá trị mặc định.
     */
    private <T> T coalesce(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }

    @Override
    public List<SummarySalaryDto> getSummarySalary(String period, Date fromDate, Date toDate) {
        BooleanBuilder builder = new BooleanBuilder()
                .and(truckEntity.driverId.eq(userEntity.id))
                .and(scheduleEntity.departureTime.between(fromDate, toDate))
                .and(scheduleEntity.type.eq(ScheduleTypeEnum.PAYROLL.getValue()))
                .and(truckEntity.deleted.eq(false))
                .and(scheduleConfigEntity.deleted.eq(false))
                .and(scheduleEntity.deleted.eq(false));

        Expression<Float> sumTotalSchedules = JPAExpressions
                .select(scheduleConfigEntity.amount.sum().coalesce(0F))
                .from(truckEntity)
                .join(scheduleEntity).on(truckEntity.licensePlate.eq(scheduleEntity.truckLicense))
                .join(scheduleConfigEntity).on(scheduleEntity.scheduleConfigId.eq(scheduleConfigEntity.id))
                .where(builder);

        Expression<Float> sumSalaryDeduction = JPAExpressions
                .select(
                        salaryEntity.mandatoryInsurance
                                .add(salaryEntity.tradeUnion)
                                .add(salaryEntity.advance)
                                .add(salaryEntity.errorOfDriver)
                                .add(salaryEntity.deductionSnn)
                                .coalesce(0F)
                )
                .from(salaryEntity)
                .where(
                        salaryEntity.userId.eq(userEntity.id)
                                .and(salaryEntity.period.eq(period))
                );

        // Subquery for sumSalaryReceived
        Expression<Float> sumSalaryReceived = JPAExpressions
                .select(
                        salaryEntity.phoneAllowance
                                .add(salaryEntity.basicSalary)
                                .add(salaryEntity.jobAllowance)
                                .add(salaryEntity.bonus)
                                .add(salaryEntity.monthlyPaidLeave)
                                .add(salaryEntity.ot)
                                .add(salaryEntity.receivedSnn)
                                .add(salaryEntity.unionContribution)
                                .add(salaryEntity.travelExpensesReimbursement)
                                .coalesce(0F)
                )
                .from(salaryEntity)
                .where(
                        salaryEntity.userId.eq(user.id)
                                .and(salaryEntity.period.eq(period))
                );

        NumberExpression<Float> netSalary = Expressions
                .numberTemplate(Float.class, "{0} - {1} + {2}",
                        sumTotalSchedules,
                        sumSalaryDeduction,
                        sumSalaryReceived
                ).coalesce(0F);

        return query
                .select(Projections.constructor(
                        SummarySalaryDTO.class,
                        user.id,
                        user.fullName,
                        sumTotalSchedules,
                        sumSalaryDeduction,
                        sumSalaryReceived,
                        netSalary
                ))
                .from(user)
                .where(user.status.eq(1)) // Chỉ lấy người dùng đang hoạt động
                .groupBy(user.id, user.fullName)
                .fetch();
    }
}