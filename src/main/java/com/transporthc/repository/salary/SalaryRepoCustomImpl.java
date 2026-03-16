package com.transporthc.repository.salary;

import com.transporthc.dto.salary.SalaryUserDto;
import com.transporthc.dto.salary.UpdateSalaryDto;
import com.transporthc.repository.BaseRepo;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAUpdateClause;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.time.YearMonth;
import java.util.List;

import static com.transporthc.entity.salary.QSalaryEntity.salaryEntity;
import static com.transporthc.entity.user.QUserEntity.userEntity;

@Repository
public class SalaryRepoCustomImpl extends BaseRepo implements SalaryRepoCustom {
    public SalaryRepoCustomImpl(EntityManager entityManager) {
        super(entityManager);
    }

    private final Expression<SalaryUserDto> expression = Projections.fields(SalaryUserDto.class,
            salaryEntity.id,
            userEntity.fullName,
            salaryEntity.period,
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
    );

    public List<SalaryUserDto> getAllSalaryWithUserPeriod(String period){
        return query.select(expression)
                .from(salaryEntity)
                .join(userEntity).on(salaryEntity.userId.eq(userEntity.id))
                .where(salaryEntity.period.eq(period))
                .fetch();
    }

    public SalaryUserDto getSalaryWithUser(Integer id){
        return query.select(expression)
                .from(salaryEntity)
                .join(userEntity).on(salaryEntity.userId.eq(userEntity.id))
                .where(salaryEntity.id.eq(id))
                .fetchOne();
    }

    @Transactional
    public Boolean updateSalary(Integer id, UpdateSalaryDto updateSalaryDTO) {
        BooleanBuilder whereClause = new BooleanBuilder();
        whereClause.and(salaryEntity.id.eq(id));

        JPAUpdateClause updateClause = query.update(salaryEntity).where(whereClause);

        if (updateSalaryDTO.getPhoneAllowance() != null) {
            updateClause.set(salaryEntity.phoneAllowance, updateSalaryDTO.getPhoneAllowance());
        }
        if (updateSalaryDTO.getBasicSalary() != null) {
            updateClause.set(salaryEntity.basicSalary, updateSalaryDTO.getBasicSalary());
            updateClause.set(salaryEntity.mandatoryInsurance, updateSalaryDTO.getBasicSalary() * 0.105F);
            updateClause.set(salaryEntity.tradeUnion, updateSalaryDTO.getBasicSalary() * 0.01F);
        }
        if (updateSalaryDTO.getJobAllowance() != null) {
            updateClause.set(salaryEntity.jobAllowance, updateSalaryDTO.getJobAllowance());
        }
        if (updateSalaryDTO.getBonus() != null) {
            updateClause.set(salaryEntity.bonus, updateSalaryDTO.getBonus());
        }
        if (updateSalaryDTO.getMonthlyPaidLeave() != null) {
            updateClause.set(salaryEntity.monthlyPaidLeave, updateSalaryDTO.getMonthlyPaidLeave());
        }
        if (updateSalaryDTO.getOt() != null) {
            updateClause.set(salaryEntity.ot, updateSalaryDTO.getOt());
        }
        if (updateSalaryDTO.getReceivedSnn() != null) {
            updateClause.set(salaryEntity.receivedSnn, updateSalaryDTO.getReceivedSnn());
        }
        if (updateSalaryDTO.getUnionContribution() != null) {
            updateClause.set(salaryEntity.unionContribution, updateSalaryDTO.getUnionContribution());
        }
        if (updateSalaryDTO.getTravelExpensesReimbursement() != null) {
            updateClause.set(salaryEntity.travelExpensesReimbursement, updateSalaryDTO.getTravelExpensesReimbursement());
        }
        if (updateSalaryDTO.getAdvance() != null) {
            updateClause.set(salaryEntity.advance, updateSalaryDTO.getAdvance());
        }
        if (updateSalaryDTO.getErrorOfDriver() != null) {
            updateClause.set(salaryEntity.errorOfDriver, updateSalaryDTO.getErrorOfDriver());
        }
        if (updateSalaryDTO.getDeductionSnn() != null) {
            updateClause.set(salaryEntity.deductionSnn, updateSalaryDTO.getDeductionSnn());
        }

        return updateClause.execute() > 0;
    }

    @Transactional
    public void createSalaryForAllUsers() {
        YearMonth currentPeriod = YearMonth.now();
        String period = currentPeriod.toString();

        String sql = "INSERT INTO salary (user_id, period, phone_allowance, basic_salary, job_allowance, bonus, " +
                "monthly_paid_leave, ot, received_snn, union_contribution, travel_expenses_reimbursement, " +
                "mandatory_insurance, trade_union, advance, error_of_driver, deduction_snn) " +
                "SELECT u.id, ?, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 FROM users u";

        entityManager.createNativeQuery(sql)
                .setParameter(1, period)
                .executeUpdate();
    }
}
