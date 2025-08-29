package com.transporthc.service.schedule.schedule;

import com.transporthc.dto.attached.AttachedImgPathDto;
import com.transporthc.dto.ExportExcelResponse;
import com.transporthc.dto.schedule.ScheduleDto;
import com.transporthc.dto.schedule.ScheduleSalaryDto;
import com.transporthc.entity.schedule.Schedule;
import com.transporthc.entity.truck.Truck;
import com.transporthc.enums.attached.AttachedTypeEnum;
import com.transporthc.enums.permission.PermissionKeyEnum;
import com.transporthc.enums.permission.PermissionTypeEnum;
import com.transporthc.enums.schedule.ScheduleStatusEnum;
import com.transporthc.enums.schedule.ScheduleTypeEnum;
import com.transporthc.enums.truck.TruckStatusEnum;
import com.transporthc.enums.truck.TruckTypeEnum;
import com.transporthc.exception.define.*;
import com.transporthc.mapper.schedule.ScheduleMapper;
import com.transporthc.repository.schedule.schedule.ScheduleRepo;
import com.transporthc.repository.truck.TruckRepo;
import com.transporthc.service.BaseService;
import com.transporthc.service.attached.AttachedImgService;
import com.transporthc.service.NotificationService;
import com.transporthc.utils.ExcelUtils;
import com.transporthc.utils.ExportConfig;
import com.transporthc.utils.FileFactory;
import com.transporthc.utils.ImportConfig;
import com.transporthc.utils.utils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.rmi.ServerException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl extends BaseService implements ScheduleService {
    private final ScheduleRepo scheduleRepo;
    private final TruckRepo truckRepo;
    private final ScheduleMapper scheduleMapper;
    private final NotificationService notificationService;
    private final AttachedImgService attachedService;
    private final PermissionTypeEnum type = PermissionTypeEnum.SCHEDULES;

    private void validate(ScheduleDto dto) {
        if (dto.getType() == ScheduleTypeEnum.PAYROLL) {
            validatePayrollSchedule(dto);
        }
        validateTruck(dto.getTruckLicense(), TruckTypeEnum.TRUCK_HEAD);
        validateTruck(dto.getMoocLicense(), TruckTypeEnum.MOOC);
    }

    private void validatePayrollSchedule(ScheduleDto dto) {
        if (dto.getScheduleConfigId().isBlank()) {
            throw new InvalidFieldException("Cấu hình lịch trình không được để trống!");
        }
        if (dto.getDepartureTime() == null) {
            throw new InvalidFieldException("Thời gian lấy hàng không được để trống!");
        } else {
            if (dto.getDepartureTime().before(new Date())) {
                throw new InvalidFieldException("Thời gian khởi hành không hợp lệ. Thời gian chỉ được tính sau thời điểm lịch trình được tạo!");
            }
        }
    }

    private void validateTruck(String license, TruckTypeEnum type) {
        String message = null;
        Truck truck = truckRepo.findByLicensePlate(license)
                .orElseThrow(() -> new NotFoundException("Xe có biển số " + license + " không tồn tại!"));
        if (!truck.getType().equals(type.getValue())) {
            message = String.format("Loại xe đang chọn không hợp lệ. Vui lòng chọn %s!", type.getDescription());
        } else if (!truck.getStatus().equals(TruckStatusEnum.AVAILABLE.getValue())) { //Check trang thai xe neu sung loai xe
            message = type.getDescription() + " được chọn không có sẵn để lên lịch. Vui lòng chọn xe khác!";
        }
        if (message != null) {
            throw new InvalidFieldException(message);
        }
    }

    @Override
    public List<ScheduleDto> getAll(Integer page, String driverId, String truckLicense, String fromDateStr, String toDateStr) {
        checkPermission(type, PermissionKeyEnum.VIEW);
        Date[] range = utils.createDateRange(fromDateStr, toDateStr);
        if (page != null) {
            if (page <= 0) {
                throw new InvalidParameterException("Vui lòng chọn trang bắt đầu từ 1!");
            } else {
                return scheduleRepo.getAll(page, driverId, truckLicense, range[0], range[1]);
            }
        } else {
            return scheduleRepo.getAll(driverId, truckLicense, range[0], range[1]);
        }
    }

    @Override
    public ScheduleDto getByID(String id) {
        checkPermission(type, PermissionKeyEnum.VIEW);
        return scheduleRepo.getByID(id)
                .orElseThrow(() -> new NotFoundException("Lịch trình cần tìm không tồn tại!"));
    }

    @Override
    @Transactional
    public ScheduleDto create(ScheduleDto dto) throws ServerException {
        checkPermission(type, PermissionKeyEnum.WRITE);
        validate(dto);
        Schedule schedule = createScheduleFromDto(dto);

        // Gửi notification qua WebSocket
        String notifyMsg = "Lịch trình mới được khởi tạo cần được phê duyệt lúc " + new java.util.Date();
        notificationService.sendNotification("{\"message\":\"" + notifyMsg + "\"}");

        Optional<ScheduleDto> result = scheduleRepo.getByID(schedule.getId());
        if (result.isEmpty()) {
            throw new ServerException("Có lỗi khi tạo lịch trình mới. Vui lòng thử lại sau!");
        }
        return result.get();
    }

    private Schedule createScheduleFromDto(ScheduleDto dto) {
        Schedule schedule = scheduleMapper.toSchedule(dto);
        scheduleRepo.save(schedule);
        return schedule;
    }

    @Override
    public ScheduleDto update(String id, ScheduleDto dto) {
        checkPermission(type, PermissionKeyEnum.WRITE);

        Schedule schedule = scheduleRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Lịch trình cần cập nhật không tồn tại!"));
        checkUpdateConditions(schedule);

        updateScheduleFromDto(schedule, dto);

        Optional<ScheduleDto> result = scheduleRepo.getByID(id);
        return result.orElse(null);
    }

    private void updateScheduleFromDto(Schedule schedule, ScheduleDto dto) {
        scheduleMapper.updateSchedule(schedule, dto);
        if (dto.getTruckLicense() != null) {
            validateTruck(dto.getTruckLicense(), TruckTypeEnum.TRUCK_HEAD);
        }
        if (dto.getMoocLicense() != null) {
            validateTruck(dto.getMoocLicense(), TruckTypeEnum.MOOC);
        }
        scheduleRepo.save(schedule);
    }

    private void checkUpdateConditions(Schedule schedule) {
        //Chỉ sửa được trước ngày bắt đầu (departure time) hoặc chưa duyệt
        Date currentTime = new Date();
        if (
                ScheduleStatusEnum.valueOf(schedule.getStatus()) != ScheduleStatusEnum.PENDING
                        || (schedule.getDepartureTime() != null && schedule.getDepartureTime().before(currentTime))) {
            throw new ConflictException("Lịch trình đã hết thời gian được phép chỉnh sửa!");
        }
    }

    @Override
    public long deleteByID(String id) throws ServerException {
        checkPermission(type, PermissionKeyEnum.DELETE);
        if (scheduleRepo.countByID(id) == 0) {
            throw new NotFoundException("Lịch trình cần duyệt không tồn tại!");
        }
        long numOfRowsDeleted = scheduleRepo.delete(id);
        if (numOfRowsDeleted == 0) {
            throw new ServerException("Có lỗi khi xóa lịch trình. Vui lòng thử lại sau!");
        }
        return numOfRowsDeleted;
    }

    @Override
    public long approveByID(String id, boolean approved) throws ServerException {
        checkPermission(type, PermissionKeyEnum.APPROVE);
        checkApproveConditions(id);
        return approveSchedule(id, approved);
    }

    private void checkApproveConditions(String id) {
        ScheduleStatusEnum status = scheduleRepo.getStatusByID(id);
        if (status == null) {
            throw new NotFoundException("Lịch trình không tồn tại!");
        } else if (status != ScheduleStatusEnum.PENDING) {
            throw new NotModifiedException("Lịch trình đã được xử lý trước đó!");
        }
    }

    private long approveSchedule(String id, boolean approved) throws ServerException {
        long result = scheduleRepo.approve(id, approved);
        if (result == 0) {
            throw new ServerException("Có lỗi khi duyệt lịch trình. Vui lòng thử lại sau!");
        }
        return result;
    }

    @Override
    @Transactional
    public long markComplete(String id, AttachedImgPathDto attachedImagePathsDto) throws ServerException {
        checkMarkCompleteConditions(id);
        attachedService.addAttachedImages(id, AttachedTypeEnum.ATTACHED_OF_SCHEDULE, attachedImagePathsDto);
        return mark(id);
    }

    private void checkMarkCompleteConditions(String id) {
        ScheduleStatusEnum status = scheduleRepo.getStatusByID(id);
        if (status == null) {
            throw new NotFoundException("Lịch trình không tồn tại!");
        }
        switch (status) {
            case ScheduleStatusEnum.PENDING, ScheduleStatusEnum.REJECTED ->
                    throw new ConflictException("Lịch trình chưa/không được duyệt để di chuyển!");
            case ScheduleStatusEnum.COMPLETED ->
                    throw new NotModifiedException("Chuyến đi đã được đánh dấu là hoàn thành trước đó!");
        }
    }

    private long mark(String id) throws ServerException {
        long result = scheduleRepo.markComplete(id);
        if (result == 0) {
            throw new ServerException("Đã có lỗi xảy ra. Vui lòng thử lại sau!");
        }
        return result;
    }

    @Override
    public List<ScheduleDto> report(String license, String period) {
        checkPermission(PermissionTypeEnum.REPORTS, PermissionKeyEnum.VIEW);
        Date[] range = utils.createDateRange(period);
        return scheduleRepo.exportReport(license, range[0], range[1]);
    }

    @Override
    public List<ScheduleSalaryDto> exportScheduleSalary (String driverId, String period) {
        checkPermission(PermissionTypeEnum.REPORTS, PermissionKeyEnum.VIEW);
        Date[] range = utils.createDateRange(period);
        return scheduleRepo.exportScheduleSalary(driverId, range[0], range[1]);
    }

    @Override
    public List<Schedule> importScheduleData(MultipartFile importFile) {
        checkPermission(type, PermissionKeyEnum.WRITE);

        Workbook workbook = FileFactory.getWorkbookStream(importFile);
        List<ScheduleDto> scheduleDtoList = ExcelUtils.getImportData(workbook, ImportConfig.scheduleImport);

        List<Schedule> schedule = scheduleMapper.toScheduleList(scheduleDtoList);
        return scheduleRepo.saveAll(schedule);
    }

    @Override
    public ExportExcelResponse exportSchedule(String driverId, String truckLicense, String fromDate, String toDate) throws Exception {
        Date[] range = utils.createDateRange(fromDate, toDate);
        List<ScheduleDto> schedule = scheduleRepo.getAll(driverId, truckLicense, range[0], range[1]);

        if (CollectionUtils.isEmpty(schedule)) {
            throw new NotFoundException("No data");
        }
        String fileName = "Schedule Export" + ".xlsx";

        ByteArrayInputStream in = ExcelUtils.export(schedule, fileName, ExportConfig.scheduleExport);

        InputStreamResource inputStreamResource = new InputStreamResource(in);
        return new ExportExcelResponse(fileName, inputStreamResource);
    }
}