package com.transporthc.mapper.warehouses;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.transporthc.dto.warehouse.WarehousesDto;
import com.transporthc.entity.warehouse.Warehouses;

@Component
public class WarehousesMapper {
    public List<WarehousesDto> toWarehouseDTOList(List<Warehouses> warehousesList) {
        if(warehousesList == null || warehousesList.isEmpty()) return null;
        return warehousesList.stream().map(warehousesEntity->WarehousesDto.builder()
        .id(warehousesEntity.getId())
        .name(warehousesEntity.getName())
        .note(warehousesEntity.getNotes())
        .createdAt(warehousesEntity.getCreatedAt())
        .updatedAt(warehousesEntity.getUpdateAt())
        .build()).collect(Collectors.toList());
    }
}
