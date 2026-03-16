package com.transporthc.service.products;

import com.transporthc.dto.products.ProductsDto;
import com.transporthc.dto.ExportExcelResponse;
import com.transporthc.enums.permission.PermissionKeyEnum;
import com.transporthc.enums.permission.PermissionTypeEnum;
import com.transporthc.exception.define.NotFoundException;
import com.transporthc.repository.products.products.ProductsRepo;
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
public class ProductsServiceImpl extends BaseService implements ProductsService {

    private final ProductsRepo repository;
    private final PermissionTypeEnum type = PermissionTypeEnum.GOODS;

    @Override
    public List<ProductsDto> getProductsByFilter(String warehouseId) {
        checkPermission(type, PermissionKeyEnum.VIEW);
        return repository.getProductsByFilter(warehouseId);
    }

    @Override
    public ExportExcelResponse exportProducts(String warehouseId) throws Exception {
        List<ProductsDto> products = repository.getProductsByFilter(warehouseId);

        if (CollectionUtils.isEmpty(products)) {
            throw new NotFoundException("No data");
        }
        String fileName = "Products Export" + ".xlsx";

        ByteArrayInputStream in = ExcelUtils.export(products, fileName, ExportConfig.productsExport);

        InputStreamResource inputStreamResource = new InputStreamResource(in);
        return new ExportExcelResponse(fileName, inputStreamResource);
    }
}