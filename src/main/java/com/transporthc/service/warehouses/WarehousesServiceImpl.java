package com.transporthc.service.warehouses;

import com.transporthc.dto.warehouse.WarehousesDto;
import com.transporthc.dto.ExportExcelResponse;
import com.transporthc.entity.warehouse.Warehouses;
import com.transporthc.enums.permission.PermissionKeyEnum;
import com.transporthc.enums.permission.PermissionTypeEnum;
import com.transporthc.exception.define.NotFoundException;
import com.transporthc.mapper.warehouses.WarehousesMapper;
import com.transporthc.repository.warehouses.WarehousesRepo;
import com.transporthc.service.BaseService;
import com.transporthc.utils.ExcelUtils;
import com.transporthc.utils.ExportConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayInputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WarehousesServiceImpl extends BaseService implements WarehousesService {

    private final WarehousesRepo repository;
    private final WarehousesMapper mapper;
    private final PermissionTypeEnum type = PermissionTypeEnum.WAREHOUSES;

    @Override
    public List<WarehousesDto> getAllWarehouses() {

        checkPermission(type, PermissionKeyEnum.VIEW);
        List<WarehousesDto> warehousesList = repository.getAll();
        return warehousesList;
    }

    @Override
    public ExportExcelResponse exportWarehouses() throws Exception {
        List<Warehouses> warehousesList = repository.findAll();
        List<WarehousesDto> warehouses = mapper.toWarehouseDTOList(warehousesList);

        if (CollectionUtils.isEmpty(warehouses)) {
            throw new NotFoundException("No data");
        }
        String fileName = "Warehouses Export" + ".xlsx";

        ByteArrayInputStream in = ExcelUtils.export(warehouses, fileName, ExportConfig.warehouseExport);

        InputStreamResource inputStreamResource = new InputStreamResource(in);
        return new ExportExcelResponse(fileName, inputStreamResource);
    }
}