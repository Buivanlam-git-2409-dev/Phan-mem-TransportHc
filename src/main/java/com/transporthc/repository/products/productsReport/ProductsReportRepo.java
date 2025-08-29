package com.transporthc.repository.products.productsReport;

import org.springframework.stereotype.Repository;

import com.transporthc.entity.products.ProductsReport;

import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface ProductsReportRepo extends JpaRepository<ProductsReport, Long>, ProductsReportRepoCustom{
    
}
