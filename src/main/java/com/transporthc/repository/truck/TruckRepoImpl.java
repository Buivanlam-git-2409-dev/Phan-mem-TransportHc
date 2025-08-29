package com.transporthc.repository.truck;

import java.util.List;
import java.util.Optional;
import com.querydsl.core.types.Predicate;

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

import static com.transporthc.entity.truck.QTruck.truck;
import static com.transporthc.entity.user.QUser.user;

@Repository
public class TruckRepoImpl extends BaseRepo implements TruckRepoCustom {
    public TruckRepoImpl(EntityManager entityManager) {
        super(entityManager);
    }

    private ConstructorExpression<TruckDto> expression() {
        return Projections.constructor(TruckDto.class,
                truck.id.as("id"),
                truck.licensePlate.as("licensePlate"),
                truck.capacity.as("capacity"),
                truck.driverId.as("driverId"),
                user.fullName.as("driverName"),
                truck.type.as("type"),
                new CaseBuilder()
                        .when(truck.type.eq(TruckTypeEnum.TRUCK_HEAD.getValue())).then(TruckTypeEnum.TRUCK_HEAD.getDescription())
                        .when(truck.type.eq(TruckTypeEnum.MOOC.getValue())).then(TruckTypeEnum.MOOC.getDescription())
                        .otherwise("Không xác định")
                        .as("typeDescription"),
                truck.status.as("status"),
                new CaseBuilder()
                        .when(truck.status.eq(TruckStatusEnum.APPROVED_SCHEDULE.getValue())).then(TruckStatusEnum.APPROVED_SCHEDULE.getDescription())
                        .when(truck.status.eq(TruckStatusEnum.AVAILABLE.getValue())).then(TruckStatusEnum.AVAILABLE.getDescription())
                        .when(truck.status.eq(TruckStatusEnum.MAINTENANCE.getValue())).then(TruckStatusEnum.MAINTENANCE.getDescription())
                        .when(truck.status.eq(TruckStatusEnum.PENDING_SCHEDULE.getValue())).then(TruckStatusEnum.PENDING_SCHEDULE.getDescription())
                        .otherwise("Không xác định")
                        .as("statusDescription"),
                truck.note.as("note"),
                truck.createdAt.as("createdAt"),
                truck.updatedAt.as("updatedAt")
        );
    }
    // ham dkien
    private BooleanBuilder createBasePredicate() {
        return new BooleanBuilder(truck.deleted.eq(false));
    }
    // ham phuong thuc truy van
    private List<TruckDto> fetchTrucksByPredicate(Predicate predicate) {
        return query.from(truck)
                .innerJoin(user).on(truck.driverId.eq(user.id))
                .where(predicate)
                .select(expression())
                .fetch();
    }
    @Override
    public Optional<TruckDto> getTruckById(Integer id) {
        BooleanBuilder builder = createBasePredicate();
        builder.and(truck.id.eq(id));
        return Optional.ofNullable(fetchTrucksByPredicate(builder).stream().findFirst().orElse(null));
    }

    @Override
    public Optional<TruckDto> getTruckByLicense(String licensePlate) {
        BooleanBuilder builder = createBasePredicate();
        builder.and(truck.licensePlate.eq(licensePlate));
        return Optional.ofNullable(fetchTrucksByPredicate(builder).stream().findFirst().orElse(null));
    }

    @Override
    public List<TruckDto> getAllTrucks() {
        BooleanBuilder builder = createBasePredicate();
       return fetchTrucksByPredicate(builder);
    }
    
   @Override
   public List<TruckDto> getTrucksByType(Integer type) {
        BooleanBuilder builder = createBasePredicate();
        builder.and(truck.type.eq(type));
        return fetchTrucksByPredicate(builder);
}
    @Override
    @Modifying
    @Transactional
    public long delete(Integer id) {
        BooleanBuilder builder = new BooleanBuilder()
                .and(truck.id.eq(id))
                .and(truck.deleted.eq(false));
        return query.update(truck)
                .where(builder)
                .set(truck.deleted, true)
                .execute();
    }

    
}