package com.transporthc.mapper.truck;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.transporthc.dto.truck.TruckDto;
import com.transporthc.entity.truck.Truck;
import com.transporthc.enums.truck.TruckStatusEnum;
import com.transporthc.enums.truck.TruckTypeEnum;

@Component
public class TruckMapper {
    public Truck toTruck(TruckDto truckDTO) {
        if(truckDTO==null)  return null;
        return Truck.builder()
                .id(truckDTO.getId() != null ? truckDTO.getId() : null)
                .driverId(truckDTO.getDriverId())
                .licensePlate(truckDTO.getLicensePlate())
                .capacity(truckDTO.getCapacity())
                .type(truckDTO.getType().getValue())
                .note(truckDTO.getNote())
                .status(TruckStatusEnum.AVAILABLE.getValue())
                .build();
    }

    public List<Truck> toTruckList(List<TruckDto> dtos) {
        if(dtos == null || dtos.isEmpty()) {
            return Collections.emptyList();
        }
        return dtos.stream().map(this::toTruck).collect(Collectors.toList());
    }

    public TruckDto toTruckDTO(Truck truck) {
        if (truck == null) return null;
        TruckDto truckDTO = new TruckDto();
        truckDTO.setId(truck.getId());
        truckDTO.setDriverId(truck.getDriverId());
        truckDTO.setLicensePlate(truck.getLicensePlate());
        truckDTO.setCapacity(truck.getCapacity());
        truckDTO.setType(TruckTypeEnum.valueOf(truck.getType()));
        truckDTO.setNote(truck.getNote());
        truckDTO.setStatus(TruckStatusEnum.valueOf(truck.getStatus()));
        truckDTO.setCreatedAt(truck.getCreatedAt());
        truckDTO.setUpdatedAt(truck.getUpdateAt());
        return truckDTO;
    }
}
