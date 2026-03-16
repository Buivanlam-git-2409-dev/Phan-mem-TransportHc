package com.transporthc.repository.truck;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.transporthc.entity.truck.Truck;

import jakarta.validation.constraints.Size;

@Repository
public interface TruckRepo extends JpaRepository<Truck, Integer>, TruckRepoCustom{
    Optional<Truck> findByLicensePlate(String license);
    boolean existsByLicensePlate(@Size(min = 8, message = "Biển số xe tối thiểu 8 ký tự") String licensePlate);
}