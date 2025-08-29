package com.transporthc.repository.warehouses;

import org.springframework.stereotype.Repository;

import com.transporthc.entity.warehouse.Warehouses;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface WarehousesRepo extends JpaRepository<Warehouses, String>, WarehousesRepoCustom {

}