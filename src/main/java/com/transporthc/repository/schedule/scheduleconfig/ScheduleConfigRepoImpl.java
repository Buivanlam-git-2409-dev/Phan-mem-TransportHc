package com.transporthc.repository.schedule.scheduleconfig;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.transporthc.dto.schedule.ScheduleConfigDto;
import com.transporthc.enums.Pagination;
import com.transporthc.repository.BaseRepo;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import static com.transporthc.entity.schedule.QScheduleConfigEntity.scheduleConfigEntity;

import java.util.List;
import java.util.Optional;

@Repository
public class ScheduleConfigRepoImpl extends BaseRepo implements ScheduleConfigRepoCustom {
    public ScheduleConfigRepoImpl(EntityManager entityManager) {
        super(entityManager);
    }

    private final ConstructorExpression<ScheduleConfigDto> constructorExpression =
            Projections.constructor(ScheduleConfigDto.class,
                    scheduleConfigEntity.id.as("id"),
                    scheduleConfigEntity.placeA.as("placeA"),
                    scheduleConfigEntity.placeB.as("placeB"),
                    scheduleConfigEntity.amount.as("amount"),
                    scheduleConfigEntity.note.as("note"),
                    scheduleConfigEntity.createdAt.as("createdAt"),
                    scheduleConfigEntity.updatedAt.as("updatedAt"));

    @Override
    public List<ScheduleConfigDto> getAll(int page) {
        long offset = (long) (page - 1) * Pagination.TEN.getSize();
        return query.from(scheduleConfigEntity)
                .where(scheduleConfigEntity.deleted.eq(false))
                .select(constructorExpression)
                .offset(offset)
                .limit(Pagination.TEN.getSize())
                .fetch();
    }

    @Override
    public List<ScheduleConfigDto> getAll() {
        return query.from(scheduleConfigEntity)
                .where(scheduleConfigEntity.deleted.eq(false))
                .select(constructorExpression)
                .fetch();
    }

    BooleanBuilder initGetOneBuilder(String id) {
        return new BooleanBuilder()
                .and(scheduleConfigEntity.id.eq(id))
                .and(scheduleConfigEntity.deleted.eq(false));
    }

    @Override
    public Optional<ScheduleConfigDto> getByID(String id) {
        BooleanBuilder builder = initGetOneBuilder(id);
        return Optional.ofNullable(
                query.from(scheduleConfigEntity)
                        .where(builder)
                        .select(constructorExpression)
                        .fetchOne()
        );
    }

    @Override
    @Modifying
    @Transactional
    public long delete(String id) {
        BooleanBuilder builder = initGetOneBuilder(id);
        return query.update(scheduleConfigEntity)
                .where(builder)
                .set(scheduleConfigEntity.deleted, true)
                .execute();
    }

    @Override
    public long countByID(String id) {
        BooleanBuilder builder = initGetOneBuilder(id);
        Long res = query.from(scheduleConfigEntity)
                .where(builder)
                .select(scheduleConfigEntity.id.count().coalesce(0L))
                .fetchOne();

        return res == null ? 0 : res;
    }
}