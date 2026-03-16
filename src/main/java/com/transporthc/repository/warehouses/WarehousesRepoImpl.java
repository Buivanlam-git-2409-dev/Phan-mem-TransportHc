package com.transporthc.repository.warehouses;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.transporthc.dto.warehouse.WarehousesDto;
import com.transporthc.repository.BaseRepo;

import static com.transporthc.entity.warehouse.QWarehousesEntity.warehousesEntity;

import java.util.List;

import jakarta.persistence.EntityManager;

@Repository
public class WarehousesRepoImpl extends BaseRepo implements WarehousesRepoCustom{
    public WarehousesRepoImpl(EntityManager entityManager){
        super(entityManager);
    }

    private ConstructorExpression<WarehousesDto> warehousesProjection(){
        return Projections.constructor(WarehousesDto.class,
        warehousesEntity.id.as("id"),
        warehousesEntity.name.as("name"),
        warehousesEntity.notes.as("note"),
        warehousesEntity.createdAt.as("created_at"),
        warehousesEntity.updatedAt.as("updated_at")
        );
    }
    @Override
    public List<WarehousesDto> getAll(){
        return query.from(warehousesEntity).select(warehousesProjection())
        .fetch();
    }
}
