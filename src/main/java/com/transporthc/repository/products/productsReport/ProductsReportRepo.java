package com.transporthc.repository.products.productsReport;

import org.springframework.stereotype.Repository;

import com.transporthc.entity.products.ProductsReportEntity;

import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface ProductsReportRepo extends JpaRepository<ProductsReportEntity, Long>, ProductsReportRepoCustom{
    
}
