package com.transporthc.service.products;

import java.util.List;

import org.springframework.stereotype.Service;

import com.transporthc.dto.ExportExcelResponse;
import com.transporthc.dto.products.ProductsDto;

@Service
public interface ProductsService {
    List<ProductsDto> getProductsByFilter(String warehouseId);
    ExportExcelResponse exportProducts(String warehouseId) throws Exception;
}
