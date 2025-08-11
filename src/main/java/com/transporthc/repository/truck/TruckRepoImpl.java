package com.transporthc.repository.truck;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.transporthc.dto.truck.TruckDto;
import com.transporthc.enums.truck.TruckStatusEnum;
import com.transporthc.enums.truck.TruckTypeEnum;
import com.transporthc.repository.BaseRepo;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import static com.transporthc.entity.truck.QTruckEntity.truckEintty;
import static com.transporthc.entity.user.QUserEntity.userEntity;

@Repository
public class TruckRepoImpl extends BaseRepo implements TruckRepoCustom {
    public TruckRepoImpl(EntityManager entityManager) {
        super(entityManager);
    }

    private ConstructorExpression<TruckDto> expression() {
        return Projections.constructor(TruckDto.class,
                truckEntity.id.as("id"),
                truckEntity.licensePlate.as("licensePlate"),
                truckEntity.capacity.as("capacity"),
                truckEntity.driverId.as("driverId"),
                user.fullName.as("driverName"),
                truckEntity.type.as("type"),
                new CaseBuilder()
                        .when(truckEntity.type.eq(TruckTypeEnum.TRUCK_HEAD.getValue())).then(TruckTypeEnum.TRUCK_HEAD.getDescription())
                        .when(truckEntity.type.eq(TruckTypeEnum.MOOC.getValue())).then(TruckTypeEnum.MOOC.getDescription())
                        .otherwise("Không xác định")
                        .as("typeDescription"),
                truckEntity.status.as("status"),
                new CaseBuilder()
                        .when(truckEntity.status.eq(TruckStatusEnum.APPROVED_SCHEDULE.getValue())).then(TruckStatusEnum.APPROVED_SCHEDULE.getDescription())
                        .when(truckEntity.status.eq(TruckStatusEnum.AVAILABLE.getValue())).then(TruckStatusEnum.AVAILABLE.getDescription())
                        .when(truckEntity.status.eq(TruckStatusEnum.MAINTENANCE.getValue())).then(TruckStatusEnum.MAINTENANCE.getDescription())
                        .when(truckEntity.status.eq(TruckStatusEnum.PENDING_SCHEDULE.getValue())).then(TruckStatusEnum.PENDING_SCHEDULE.getDescription())
                        .otherwise("Không xác định")
                        .as("statusDescription"),
                truckEntity.note.as("note"),
                truckEntity.createdAt.as("createdAt"),
                truckEntity.updatedAt.as("updatedAt")
        );
    }

    @Override
    public Optional<TruckDto> getTruckById(Integer id) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(truckEntity.id.eq(id));
        builder.and(truckEntity.deleted.eq(false));
        return Optional.ofNullable(
                query.from(truckEntity)
                        .innerJoin(userEntity).on(truckEntity.driverId.eq(userEntity.id))
                        .where(builder)
                        .select(expression())
                        .fetchOne()
        );
    }

    @Override
    public Optional<TruckDto> getTruckByLicense(String licensePlate) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(truckEntity.licensePlate.eq(licensePlate));
        builder.and(truckEntity.deleted.eq(false));
        return Optional.ofNullable(
                query.from(truckEntity)
                        .innerJoin(userEntity).on(truckEntity.driverId.eq(userEntity.id))
                        .where(builder)
                        .select(expression())
                        .fetchOne()
        );
    }

    @Override
    public List<TruckDto> getAllTrucks() {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(truckEntity.deleted.eq(false));
        return query.from(truckEntity)
                .innerJoin(userEntity).on(truckEntity.driverId.eq(userEntity.id))
                .where(builder)
                .select(expression())
                .fetch();
    }

    @Override
    @Modifying
    @Transactional
    public long delete(Integer id) {
        BooleanBuilder builder = new BooleanBuilder()
                .and(truckEntity.id.eq(id))
                .and(truckEntity.deleted.eq(false));
        return query.update(truckEntity)
                .where(builder)
                .set(truckEntity.deleted, true)
                .execute();
    }

    public List<TruckDto> getTrucksByType(Integer type) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(truckEntity.type.eq(type));
        builder.and(truckEntity.deleted.eq(false));
        return query.from(truckEntity)
                .innerJoin(userEntity).on(truckEntity.driverId.eq(userEntity.id))
                .where(builder)
                .select(expression())
                .fetch();
    }
}