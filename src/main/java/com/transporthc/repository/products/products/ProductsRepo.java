package com.transporthc.repository.products.products;

import com.transporthc.entity.products.Products;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductsRepo extends JpaRepository<Products, String>, ProductsRepoCustom {

}