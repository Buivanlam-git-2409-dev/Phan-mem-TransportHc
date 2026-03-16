package com.transporthc.repository.schedule.schedule;

import com.transporthc.dto.schedule.ScheduleDto;
import com.transporthc.dto.schedule.ScheduleSalaryDto;
import com.transporthc.enums.Pagination;
import com.transporthc.enums.schedule.ScheduleStatusEnum;
import com.transporthc.enums.schedule.ScheduleTypeEnum;
import com.transporthc.repository.BaseRepo;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.transporthc.entity.schedule.QScheduleEntity.scheduleEntity;
import static com.transporthc.entity.schedule.QScheduleConfigEntity.scheduleConfigEntity;
import static com.transporthc.entity.user.QUserEntity.userEntity;
import static com.transporthc.entity.truck.QTruckEntity.truckEntity;
import static com.transporthc.entity.attached.QAttachedImgEntity.attachedImgEntity;

@Repository
public class ScheduleRepoImpl extends BaseRepo implements ScheduleRepoCustom {
    public ScheduleRepoImpl(EntityManager entityManager) {
        super(entityManager);
    }

    private ConstructorExpression<ScheduleDto> scheduleProjection() {
        return Projections.constructor(ScheduleDto.class,
                scheduleEntity.id.as("id"),
                scheduleEntity.scheduleConfigIdEntity.as("scheduleConfigIdEntity"),
                scheduleConfig.placeA.as("placeA"),
                scheduleConfig.placeB.as("placeB"),
                scheduleConfig.amount.as("amount"),
                truckEntity.driverId.as("driverId"),
                JPAExpressions.select(userEntity.fullName.as("driverName"))
                        .from(userEntity)
                        .where(truckEntity.driverId.eq(userEntity.id)),
                scheduleEntity.truckLicense.as("truckLicense"),
                scheduleEntity.moocLicense.as("moocLicense"),
                scheduleEntity.departureTime.as("departureTime"),
                scheduleEntity.arrivalTime.as("arrivalTime"),
                scheduleEntity.note.coalesce("").as("note"),
                JPAExpressions.select(
                                Expressions.stringTemplate("GROUP_CONCAT({0})", attachedImgEntity.imgPath).as("attachedPaths"))
                        .from(attachedImgEntity)
                        .where(attachedImgEntity.referenceId.eq(scheduleEntity.id)),
                scheduleEntity.type.as("type"),
                scheduleEntity.status.as("status"),
                scheduleEntity.createdAt.as("createdAt"),
                scheduleEntity.updatedAt.as("updatedAt")
        );
    }

    private BooleanBuilder initGetAllBuilder(String driverId, String truckLicense, Date fromDate, Date toDate) {
        BooleanBuilder builder = new BooleanBuilder().and(scheduleEntity.deleted.eq(false));

        if (driverId != null && !driverId.isBlank()) {
            builder.and(userEntity.id.eq(driverId));
        }
        if (truckLicense != null && !truckLicense.isBlank()) {
            builder.and(scheduleEntity.truckLicense.eq(truckLicense));
        }
        if (fromDate != null && toDate != null) {
            builder.and(scheduleEntity.departureTime.between(fromDate, toDate));
        } else if (fromDate != null) {
            builder.and(scheduleEntity.departureTime.goe(fromDate));
        } else if (toDate != null) {
            builder.and(scheduleEntity.departureTime.loe(toDate));
        }

        return builder;
    }

    BooleanBuilder initGetOneBuilder(String id) {
        return new BooleanBuilder()
                .and(scheduleEntity.deleted.eq(false))
                .and(scheduleEntity.id.eq(id));
    }

    @Override
    public List<ScheduleDto> getAll(int page, String driverId, String truckLicense, Date fromDate, Date toDate) {
        BooleanBuilder builder = initGetAllBuilder(driverId, truckLicense, fromDate, toDate);
        long offset = (long) (page - 1) * Pagination.TEN.getSize();
        return query.from(scheduleEntity)
                .innerJoin(scheduleConfig).on(scheduleEntity.scheduleConfigIdEntity.eq(scheduleConfig.id))
                .innerJoin(truckEntity).on(scheduleEntity.truckLicense.eq(truckEntity.licensePlate))
                .innerJoin(userEntity).on(truckEntity.driverId.eq(userEntity.id))
                .where(builder)
                .select(scheduleProjection())
                .orderBy(scheduleEntity.updatedAt.desc())
                .offset(offset)
                .limit(Pagination.TEN.getSize())
                .fetch();
    }

    @Override
    public List<ScheduleDto> getAll(String driverId, String truckLicense, Date fromDate, Date toDate) {
        BooleanBuilder builder = initGetAllBuilder(driverId, truckLicense, fromDate, toDate);
        return query.from(scheduleEntity)
                .innerJoin(scheduleConfig).on(scheduleEntity.scheduleConfigIdEntity.eq(scheduleConfig.id))
                .innerJoin(truckEntity).on(scheduleEntity.truckLicense.eq(truckEntity.licensePlate))
                .innerJoin(userEntity).on(truckEntity.driverId.eq(userEntity.id))
                .where(builder)
                .select(scheduleProjection())
                .orderBy(scheduleEntity.updatedAt.desc())
                .fetch();
    }

    @Override
    public Optional<ScheduleDto> getByID(String id) {
        BooleanBuilder builder = initGetOneBuilder(id);
        return Optional.ofNullable(
                query.from(scheduleEntity)
                        .leftJoin(scheduleConfig).on(scheduleEntity.scheduleConfigIdEntity.eq(scheduleConfig.id))
                        .innerJoin(truckEntity).on(scheduleEntity.truckLicense.eq(truckEntity.licensePlate))
                        .where(builder)
                        .select(scheduleProjection())
                        .fetchOne()
        );
    }

    @Override
    @Modifying
    @Transactional
    public long delete(String id) {
        BooleanBuilder builder = initGetOneBuilder(id);
        return query.update(scheduleEntity)
                .where(builder)
                .set(scheduleEntity.deleted, true)
                .execute();
    }

    @Override
    @Modifying
    @Transactional
    public long approve(String id, boolean approved) {
        BooleanBuilder builder = initGetOneBuilder(id)
                .and(scheduleEntity.status.eq(ScheduleStatusEnum.PENDING.getValue()));
        ScheduleStatusEnum status = approved ? ScheduleStatusEnum.APPROVED : ScheduleStatusEnum.REJECTED;
        return query.update(scheduleEntity)
                .where(builder)
                .set(scheduleEntity.status, status.getValue())
                .execute();
    }

    @Override
    @Modifying
    @Transactional
    public long markComplete(String id) {
        BooleanBuilder builder = initGetOneBuilder(id);
        return query.update(scheduleEntity)
                .where(builder)
                .set(scheduleEntity.status, ScheduleStatusEnum.COMPLETED.getValue())
                .set(scheduleEntity.arrivalTime, new java.util.Date())
                .execute();
    }

    @Override
    public List<ScheduleSalaryDto> exportScheduleSalary(String driverId, Date fromDate, Date toDate) {
        BooleanBuilder builder = new BooleanBuilder()
                .and(userEntity.id.eq(driverId));
        if (fromDate != null && toDate != null) {
            builder.and(scheduleEntity.createdAt.between(fromDate, toDate));
        }

        ConstructorExpression<ScheduleSalaryDTO> expression = Projections.constructor(ScheduleSalaryDTO.class,
                userEntity.fullName.as("driverName"),
                scheduleConfig.placeA.as("placeA"),
                scheduleConfig.placeB.as("placeB"),
                scheduleConfig.amount.min().coalesce(0f).as("amount"),
                scheduleEntity.id.count().castToNum(Integer.class).coalesce(0).as("count"),
                scheduleConfig.amount.min().multiply(scheduleEntity.id.count()).castToNum(Float.class).coalesce(0f).as("total")
        );

        return query.from(userEntity)
                .innerJoin(truckEntity).on(userEntity.id.eq(truckEntity.driverId))
                .innerJoin(scheduleEntity).on(truckEntity.licensePlate.eq(scheduleEntity.truckLicense))
                .innerJoin(scheduleConfig).on(scheduleEntity.scheduleConfigIdEntity.eq(scheduleConfig.id))
                .where(builder)
                .groupBy(scheduleConfig.placeA, scheduleConfig.placeB)
                .select(expression)
                .fetch();
    }

    @Override
    public List<ScheduleDto> findByLicensePlate(String licensePlate) {
        BooleanBuilder builder = new BooleanBuilder()
                .and(scheduleEntity.deleted.eq(false));

        if (licensePlate != null && !licensePlate.isBlank()) {
            builder.and(scheduleEntity.truckLicense.eq(licensePlate)
                    .or(scheduleEntity.moocLicense.eq(licensePlate)));
        }

        return query.from(scheduleEntity)
                .leftJoin(scheduleConfig).on(scheduleEntity.scheduleConfigIdEntity.eq(scheduleConfig.id))
                .leftJoin(truckEntity).on(scheduleEntity.truckLicense.eq(truckEntity.licensePlate))
                .leftJoin(userEntity).on(truckEntity.driverId.eq(userEntity.id))
                .where(builder)
                .select(scheduleProjection())
                .orderBy(scheduleEntity.updatedAt.desc())
                .fetch(); // Lấy dữ liệu
    }

    @Override
    public List<ScheduleDto> exportReport(String license, Date fromDate, Date toDate) {
        ConstructorExpression<ScheduleDto> expression = Projections.constructor(ScheduleDto.class,
                scheduleEntity.scheduleConfigIdEntity.coalesce(ScheduleType.INTERNAL.getDescription()).as("scheduleConfigId"),
                scheduleConfig.placeA.as("placeA"),
                scheduleConfig.placeB.as("placeB"),
                JPAExpressions.select(truckEntity.driverId.as("driverId"))
                        .from(truckEntity)
                        .where(scheduleEntity.truckLicense.eq(truckEntity.licensePlate)),
                JPAExpressions.select(userEntity.fullName)
                        .from(userEntity, truckEntity)
                        .where(scheduleEntity.truckLicense.eq(truckEntity.licensePlate)
                                .and(truckEntity.driverId.eq(userEntity.id))
                        ),
                scheduleEntity.truckLicense.as("truckLicense"),
                scheduleEntity.moocLicense.as("moocLicense"),
                scheduleEntity.departureTime.as("departureTime"),
                scheduleEntity.arrivalTime.as("arrivalTime"),
                scheduleEntity.id.count().castToNum(Integer.class).coalesce(0).as("count")
        );

        BooleanBuilder builder = new BooleanBuilder()
                .and(scheduleEntity.truckLicense.eq(license));
        if (fromDate != null) {
            builder.and(scheduleEntity.departureTime.goe(fromDate));
        }
        if (toDate != null) {
            builder.and(scheduleEntity.departureTime.loe(toDate));
        }

        return query.from(scheduleEntity)
                .leftJoin(scheduleConfig).on(scheduleEntity.scheduleConfigIdEntity.eq(scheduleConfig.id))
                .where(builder)
                .groupBy(scheduleEntity.truckLicense, scheduleEntity.moocLicense, scheduleEntity.departureTime, scheduleEntity.arrivalTime, scheduleEntity.scheduleConfigIdEntity)
                .select(expression)
                .fetch();
    }

    @Override
    public long countByID(String id) {
        BooleanBuilder builder = initGetOneBuilder(id);
        Long res = query.from(scheduleEntity)
                .where(builder)
                .select(scheduleEntity.id.count().coalesce(0L))
                .fetchOne();
        return res != null ? res : 0;
    }

    @Override
    public ScheduleStatusEnum getStatusByID(String id) {
        BooleanBuilder builder = initGetOneBuilder(id);
        Integer statusNumber = query.from(scheduleEntity)
                .where(builder)
                .select(scheduleEntity.status)
                .fetchOne();
        return statusNumber != null ? ScheduleStatus.valueOf(statusNumber) : null;
    }
}