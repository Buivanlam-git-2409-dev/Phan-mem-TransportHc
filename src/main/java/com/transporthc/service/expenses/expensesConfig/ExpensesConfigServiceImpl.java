package com.transporthc.service.expenses.expensesConfig;

import com.transporthc.dto.ExportExcelResponse;
import com.transporthc.dto.expenses.ExpensesConfigDto;
import com.transporthc.entity.expenses.ExpenseConfig;
import com.transporthc.enums.permission.PermissionKeyEnum;
import com.transporthc.enums.permission.PermissionTypeEnum;
import com.transporthc.exception.define.NotFoundException;
import com.transporthc.mapper.expenses.ExpensesConfigMapper;
import com.transporthc.repository.expenses.expensesconfig.ExpensesConfigRepo;
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
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpensesConfigServiceImpl extends BaseService implements ExpensesConfigService{
    private final ExpensesConfigRepo expensesConfigRepo;
    private final ExpensesConfigMapper expensesConfigMapper;
    private final PermissionTypeEnum type = PermissionTypeEnum.CONFIGS;

    @Override
    public List<ExpensesConfigDto> getAll() {
        checkPermission(type, PermissionKeyEnum.VIEW);
        return expensesConfigRepo.getAll();
    }

    @Override
    public ExpensesConfigDto getByID(String id) {
        checkPermission(type, PermissionKeyEnum.VIEW);
        return expensesConfigRepo.getByID(id)
                .orElseThrow(() -> new NotFoundException("Thông tin cấu hình chi phí không tồn tại!"));
    }

    @Override
    public ExpensesConfigDto create(ExpensesConfigDto dto) {
        checkPermission(type, PermissionKeyEnum.WRITE);
        ExpenseConfig config = expensesConfigMapper.toExpenseConfigEntity(dto);
        expensesConfigRepo.save(config);
        return getByID(config.getId());
    }

    @Override
    public ExpensesConfigDto update(String id, ExpensesConfigDto dto) {
        checkPermission(type, PermissionKeyEnum.WRITE);

        ExpenseConfig config = expensesConfigRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Cấu hình chi phí không tồn tại!"));

        expensesConfigMapper.updateExpensesConfig(config, dto);

        return getByID(config.getId());
    }

    @Override
    public long deleteByID(String id) {
        checkPermission(type, PermissionKeyEnum.DELETE);

        long numOfRowDeleted = expensesConfigRepo.delete(id);
        if (numOfRowDeleted == 0) {
            throw new NotFoundException("Cấu hình chi phí không tồn tại!");
        }
        return numOfRowDeleted;
    }

    @Override
    public List<ExpenseConfig> importExpensesConfigData(MultipartFile importFile) {

        checkPermission(type, PermissionKeyEnum.WRITE);

        Workbook workbook = FileFactory.getWorkbookStream(importFile);
        List<ExpensesConfigDto> expensesConfigDTOList = ExcelUtils.getImportData(workbook, ImportConfig.expensesConfigImport);

        List<ExpenseConfig> expensesConfigs = expensesConfigMapper.toExpenseConfigEntitiesList(expensesConfigDTOList);

        // Lưu tất cả các thực thể vào cơ sở dữ liệu và trả về danh sách đã lưu
        return expensesConfigRepo.saveAll(expensesConfigs);
    }

    @Override
    public ExportExcelResponse exportExpensesConfig() throws Exception {
        List<ExpensesConfigDto> expensesConfig = expensesConfigRepo.getAll();

        if (!CollectionUtils.isEmpty(expensesConfig)) {
            String fileName = "ExpensesConfig Export" + ".xlsx";

            ByteArrayInputStream in = ExcelUtils.export(expensesConfig, fileName, ExportConfig.expensesConfigExport);

            InputStreamResource inputStreamResource = new InputStreamResource(in);
            return new ExportExcelResponse(fileName, inputStreamResource);
        } else {
            throw new NotFoundException("No data");
        }
    }
}
