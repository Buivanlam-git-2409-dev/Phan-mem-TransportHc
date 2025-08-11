package com.transporthc.repository.truck;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.transporthc.entity.truck.TruckEntity;

import jakarta.validation.constraints.Size;

@Repository
public interface TruckRepo extends JpaRepository<TruckEntity, Integer>, TruckRepoCustom{
    Optional<TruckEntity> findByLicensePlate(String license);
    boolean existsByLicensePlate(@Size(min = 8, message = "Biển số xe tối thiểu 8 ký tự") String licensePlate);
}