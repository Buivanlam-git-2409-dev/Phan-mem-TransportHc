package com.transporthc.service.truck;

import com.transporthc.dto.ExportExcelResponse;
import com.transporthc.dto.schedule.ScheduleDto;
import com.transporthc.dto.truck.TruckDto;
import com.transporthc.entity.schedule.ScheduleEntity;
import com.transporthc.entity.truck.TruckEntity;
import com.transporthc.enums.permission.PermissionKeyEnum;
import com.transporthc.enums.permission.PermissionTypeEnum;
import com.transporthc.enums.role.UserRoleEnum;
import com.transporthc.enums.truck.TruckTypeEnum;
import com.transporthc.exception.define.ConflictException;
import com.transporthc.exception.define.NotFoundException;
import com.transporthc.mapper.schedule.ScheduleMapper;
import com.transporthc.mapper.truck.TruckMapper;
import com.transporthc.repository.schedule.schedule.ScheduleRepo;
import com.transporthc.repository.truck.TruckRepo;
import com.transporthc.repository.user.UserRepo;
import com.transporthc.service.BaseService;
import com.transporthc.utils.ExcelUtils;
import com.transporthc.utils.ExportConfig;
import com.transporthc.utils.FileFactory;
import com.transporthc.utils.ImportConfig;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TruckServiceImpl extends BaseService implements TruckService {

    private final TruckRepo repository;
    private final TruckMapper mapper;
    private final PermissionTypeEnum type = PermissionTypeEnum.TRUCKS;
    private final UserRepo userRepo;
    private final ScheduleRepo scheduleRepo;

    @Override
    public TruckDto createTruck(TruckDto truckDto) {
        checkPermission(type, PermissionKeyEnum.WRITE);
        checkDriverRole(userRepo.getUserById(truckDto.getDriverId()).getRoleId());

        TruckEntity truck = mapper.toTruck(truckDto);
        repository.save(truck);
        return repository.getTruckById(truck.getId()).get();
    }

    @Override
    public List<TruckDto> getAllTrucks() {
        checkPermission(type, PermissionKeyEnum.VIEW);
        return repository.getAllTrucks();
    }

    @Override
    public List<TruckDto> getTrucksByType(TruckTypeEnum truckType) {
        checkPermission(type, PermissionKeyEnum.VIEW);
        List<TruckDto> trucks = repository.getTrucksByType(truckType.getValue());
        if (trucks.isEmpty()) {
            throw new NotFoundException("Loại xe " + truckType.getDescription() + " không tồn tại!");
        }
        return trucks;
    }

    @Override
    public TruckDto getTruckByLicensePlate(String licensePlate) {
        checkPermission(type, PermissionKeyEnum.VIEW);
        return repository.getTruckByLicense(licensePlate)
                .orElseThrow(() -> new NotFoundException("Xe có biển số " + licensePlate + " không tồn tại!"));
    }

@Override
@Transactional
public TruckDto updateTruck(Integer id, TruckDto dto) {
    checkPermission(type, PermissionKeyEnum.WRITE);
    TruckEntity existingTruck = mapper.toTruck(repository.getTruckById(id)
            .orElseThrow(() -> new NotFoundException("Không tìm thấy thông tin xe cần tìm!")));
    // Cập nhật các trường từ body (chỉ cập nhật nếu không null)
    if (dto.getDriverId() != null) {
        existingTruck.setDriverId(dto.getDriverId());
    }
    if (dto.getLicensePlate() != null) {
        // Kiểm tra xem license_plate có bị trùng không
        if (repository.existsByLicensePlate(dto.getLicensePlate()) &&
                !existingTruck.getLicensePlate().equals(dto.getLicensePlate())) {
            throw new IllegalArgumentException("Biển số xe đã tồn tại!");
        }
        String oldLicense = existingTruck.getLicensePlate();
        existingTruck.setLicensePlate(dto.getLicensePlate());

        List<ScheduleDto> schedules = scheduleRepo.findByLicensePlate(oldLicense);
        if (!schedules.isEmpty()) {
            for (ScheduleDto scheduleDto : schedules) {
                // Lấy bản ghi Schedule hiện tại từ DB
                ScheduleEntity existingSchedule = scheduleRepo.findById(scheduleDto.getId())
                        .orElseThrow(() -> new NotFoundException("Không tìm thấy lịch trình với ID: " + scheduleDto.getId()));
                if(existingSchedule.getTruckLicense().equals(oldLicense)) {
                    existingSchedule.setTruckLicense(dto.getLicensePlate());
                }
                if(existingSchedule.getMoocLicense().equals(oldLicense)) {
                    existingSchedule.setMoocLicense(dto.getLicensePlate());
                }
                scheduleRepo.save(existingSchedule);
            }
        }
    }
    if (dto.getCapacity() != null) {
        existingTruck.setCapacity(dto.getCapacity());
    }
    if (dto.getNote() != null) {
        existingTruck.setNote(dto.getNote());
    }
    if (dto.getType() != null) {
        existingTruck.setType(dto.getType().getValue());
    }
    if (dto.getStatus() != null) {
        existingTruck.setStatus(dto.getStatus().getValue());
    }
//    existingTruck.setUpdatedAt(new Date());
    repository.save(existingTruck);
    return mapper.toTruckDTO(existingTruck);
}


    @Override
    public long deleteTruck(Integer id) {
        checkPermission(type, PermissionKeyEnum.DELETE);
        TruckDto truck = repository.getTruckById(id)
                .orElseThrow(() -> new NotFoundException("Xe cần xóa không tồn tại!"));
        return repository.delete(id);
    }

    @Override
    public List<TruckEntity> importTruckData(MultipartFile importFile) {

        checkPermission(type, PermissionKeyEnum.WRITE);

        Workbook workbook = FileFactory.getWorkbookStream(importFile);
        List<TruckDto> truckDtoList = ExcelUtils.getImportData(workbook, ImportConfig.truckImport);

        for(TruckDto truckDto : truckDtoList) {
            checkDriverRole(userRepo.getUserById(truckDto.getDriverId()).getRoleId());
        }

        List<TruckEntity> trucks = mapper.toTruckList(truckDtoList);

        // Lưu tất cả các thực thể vào cơ sở dữ liệu và trả về danh sách đã lưu
        return repository.saveAll(trucks);
    }

    private void checkDriverRole(int roleID) {
        if (!UserRoleEnum.DRIVER.getId().equals(roleID)) {
            throw new ConflictException("Người chịu trách nhiệm phải là tài xế");
        }
    }

    @Override
    public ExportExcelResponse exportTruckData() throws Exception {
        List<TruckDto> trucks = repository.getAllTrucks();

        if (CollectionUtils.isEmpty(trucks)) {
            throw new NotFoundException("No data");
        }
        String fileName = "Trucks Export" + ".xlsx";

        ByteArrayInputStream in = ExcelUtils.export(trucks, fileName, ExportConfig.truckExport);

        InputStreamResource inputStreamResource = new InputStreamResource(in);
        return new ExportExcelResponse(fileName, inputStreamResource);
    }
}