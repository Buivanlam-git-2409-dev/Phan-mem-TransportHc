package com.transporthc.service.schedule.scheduleconfig;
import com.transporthc.dto.ExportExcelResponse;
import com.transporthc.dto.schedule.ScheduleConfigDto;
import com.transporthc.entity.schedule.ScheduleConfig;
import com.transporthc.enums.permission.PermissionKeyEnum;
import com.transporthc.enums.permission.PermissionTypeEnum;
import com.transporthc.exception.define.InvalidParameterException;
import com.transporthc.exception.define.NotFoundException;
import com.transporthc.mapper.schedule.ScheduleConfigMapper;
import com.transporthc.repository.schedule.scheduleconfig.ScheduleConfigRepo;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.rmi.ServerException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleConfigServiceImpl extends BaseService implements ScheduleConfigService {
    private final ScheduleConfigRepo scheduleConfigRepo;
    private final ScheduleConfigMapper scheduleConfigMapper;
    private final PermissionTypeEnum type = PermissionTypeEnum.CONFIGS;

    @Override
    public List<ScheduleConfigDto> getAll(int page) {
        checkPermission(type, PermissionKeyEnum.VIEW);
        if (page <= 0) {
            throw new InvalidParameterException("Vui lòng chọn trang bắt đầu từ 1!");
        }
        return scheduleConfigRepo.getAll(page);
    }

    @Override
    public List<ScheduleConfigDto> getAll() {
        checkPermission(type, PermissionKeyEnum.VIEW);
        return scheduleConfigRepo.getAll();
    }

    @Override
    public ScheduleConfigDto getByID(String id) {
        checkPermission(type, PermissionKeyEnum.VIEW);
        return scheduleConfigRepo.getByID(id)
                .orElseThrow(() -> new NotFoundException("Cấu hình lịch trình không tồn tại!"));
    }

    @Override
    public ScheduleConfigDto create(ScheduleConfigDto dto) {
        checkPermission(type, PermissionKeyEnum.WRITE);
        ScheduleConfig config = scheduleConfigMapper.toScheduleConfig(dto);
        scheduleConfigRepo.save(config);
        return getByID(config.getId());
    }

    @Override
    public ScheduleConfigDto update(String id, ScheduleConfigDto dto) {
        checkPermission(type, PermissionKeyEnum.WRITE);

        ScheduleConfig config = scheduleConfigRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Cấu hình lịch trình không tồn tại hoặc đã bị xóa trước đó!"));

        scheduleConfigMapper.updateScheduleConfig(config, dto);
        scheduleConfigRepo.save(config);
        return getByID(config.getId());
    }

    @Override
    public long deleteByID(String id) throws ServerException {
        checkPermission(type, PermissionKeyEnum.DELETE);
        if (scheduleConfigRepo.countByID(id) == 0) {
            throw new NotFoundException("Cấu hình lịch trình không tồn tại!");
        }
        long numOfRowsDeleted = scheduleConfigRepo.delete(id);
        if (numOfRowsDeleted == 0) {
            throw new ServerException("Đã có lỗi xảy ra. Vui lòng thử lại sau!");
        }
        return numOfRowsDeleted;
    }

    @Override
    public List<ScheduleConfig> importScheduleConfigData(MultipartFile importFile) {

        checkPermission(type, PermissionKeyEnum.WRITE);

        Workbook workbook = FileFactory.getWorkbookStream(importFile);
        List<ScheduleConfigDto> scheduleConfigDtoList = ExcelUtils.getImportData(workbook, ImportConfig.scheduleConfigImport);

        List<ScheduleConfig> scheduleConfig = scheduleConfigMapper.toScheduleConfigList(scheduleConfigDtoList);

        return scheduleConfigRepo.saveAll(scheduleConfig);
    }

    @Override
    public ExportExcelResponse exportScheduleConfig() throws Exception {
        List<ScheduleConfigDto> scheduleConfig = scheduleConfigRepo.getAll();

        if (CollectionUtils.isEmpty(scheduleConfig)) {
            throw new NotFoundException("No data");
        }
        String fileName = "ScheduleConfig Export" + ".xlsx";

        ByteArrayInputStream in = ExcelUtils.export(scheduleConfig, fileName, ExportConfig.scheduleConfigExport);

        InputStreamResource inputStreamResource = new InputStreamResource(in);
        return new ExportExcelResponse(fileName, inputStreamResource);
    }
}