package com.transporthc.service.truck;

import com.transporthc.dto.ExportExcelResponse;
import com.transporthc.dto.truck.TruckDto;
import com.transporthc.entity.truck.TruckEntity;
import com.transporthc.enums.truck.TruckTypeEnum;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TruckService {
    TruckDto createTruck(TruckDto truckDto);
    List<TruckDto> getAllTrucks();
    List<TruckDto> getTrucksByType(TruckTypeEnum type);
    TruckDto getTruckByLicensePlate(String licensePlate);
    TruckDto updateTruck(Integer id, TruckDto truckDto);
    long deleteTruck(Integer id);
    List<TruckEntity> importTruckData(MultipartFile importFile);
    ExportExcelResponse exportTruckData() throws Exception;
}