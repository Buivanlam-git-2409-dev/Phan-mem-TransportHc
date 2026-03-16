package com.transporthc.repository.warehouses;

import java.util.List;

import com.transporthc.dto.warehouse.WarehousesDto;

public interface WarehousesRepoCustom {
    List<WarehousesDto> getAll();
}
