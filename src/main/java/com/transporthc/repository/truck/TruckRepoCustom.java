package com.transporthc.repository.truck;
import com.transporthc.dto.truck.TruckDto;

import java.util.List;
import java.util.Optional;

public interface TruckRepoCustom {
    Optional<TruckDto> getTruckById(Integer id);
    Optional<TruckDto> getTruckByLicense(String licensePlate);
    List<TruckDto> getAllTrucks();
    List<TruckDto> getTrucksByType(Integer type);
    long delete(Integer id);
}
