package com.transporthc.repository.products.products;

import java.util.List;

import com.transporthc.dto.products.ProductsDto;

public interface ProductsRepoCustom {
    List<ProductsDto> getProductsByFilter(String warehouseId);
}
