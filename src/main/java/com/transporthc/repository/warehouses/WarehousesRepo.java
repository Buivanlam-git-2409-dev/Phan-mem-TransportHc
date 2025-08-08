package com.transporthc.repository.warehouses;

import org.springframework.stereotype.Repository;

import com.transporthc.entity.warehouse.WarehousesEntity;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface WarehousesRepo extends JpaRepository<WarehousesEntity, String>, WarehousesRepoCustom {

}