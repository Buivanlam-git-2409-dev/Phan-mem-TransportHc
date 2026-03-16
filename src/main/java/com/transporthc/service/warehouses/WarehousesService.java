package com.transporthc.service.warehouses;

import java.util.List;

import org.springframework.stereotype.Service;

import com.transporthc.dto.ExportExcelResponse;
import com.transporthc.dto.warehouse.WarehousesDto;

@Service
public interface WarehousesService {
    List<WarehousesDto> getAllWarehouses();
    ExportExcelResponse exportWarehouses() throws Exception;
}
