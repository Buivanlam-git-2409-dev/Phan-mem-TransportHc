package com.transporthc.service.expenses.expenses;

import com.transporthc.dto.ExportExcelResponse;
import com.transporthc.dto.expenses.ExpensesDto;
import com.transporthc.dto.expenses.ExpensesIncurredDto;
import com.transporthc.dto.expenses.ExpensesReportDto;
import com.transporthc.entity.expenses.ExpensesEntity;
import com.transporthc.enums.attached.AttachedTypeEnum;
import com.transporthc.enums.expenses.ExpensesStatusEnum;
import com.transporthc.enums.permission.PermissionKeyEnum;
import com.transporthc.enums.permission.PermissionTypeEnum;
import com.transporthc.exception.define.ConflictException;
import com.transporthc.exception.define.InvalidParameterException;
import com.transporthc.exception.define.NotFoundException;
import com.transporthc.exception.define.NotModifiedException;
import com.transporthc.mapper.expenses.ExpensesMapper;
import com.transporthc.repository.expenses.expenses.ExpensesRepo;
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
public class ExpensesServiceImpl extends BaseService implements ExpensesService{
    private final ExpensesRepo expensesRepo;
    private final ExpensesMapper expensesMapper;
    private final NotificationService notificationService;
    private final AttachedImgService attachedService;
    private final PermissionTypeEnum type = PermissionTypeEnum.EXPENSES;

    @Override
    public List<ExpensesDto> getAll(Integer page, String expensesConfigId, String truckLicense, String fromDateStr, String toDateStr) {
        checkPermission(type, PermissionKeyEnum.VIEW);
        Date[] range = utils.createDateRange(fromDateStr, toDateStr);
        if (page != null) {
            if (page <= 0) {
                throw new InvalidParameterException("Vui lòng chọn trang bắt đầu từ 1!");
            } else {
                return expensesRepo.getAll(page, expensesConfigId, truckLicense, range[0], range[1]);
            }
        } else {
            return expensesRepo.getAll(expensesConfigId, truckLicense, range[0], range[1]);
        }
    }

    @Override
    public ExpensesDto getByID(String id) {
        checkPermission(type, PermissionKeyEnum.VIEW);
        return expensesRepo.getByID(id)
                .orElseThrow(() -> new NotFoundException("Chi phí không tồn tại!"));
    }

    @Override
    @Transactional
    public ExpensesDto create(ExpensesDto dto) throws ServerException {
        checkPermission(type, PermissionKeyEnum.WRITE);

        ExpensesEntity expenses = createExpensesFromDto(dto);
        attachImages(expenses.getId(), dto);

        String notifyMsg = "Có một chi phí được tạo mới cần được phê duyệt lúc " + new Date();
        notificationService.sendNotification("{\"message\":\"" + notifyMsg + "\"}");

        Optional<ExpensesDto> result = expensesRepo.getByID(expenses.getId());
        if (result.isEmpty()) {
            throw new ServerException("Có lỗi khi tạo chi phí mới. Vui lòng thử lại sau!");
        }
        return result.get();
    }

    private void attachImages(String referenceID, ExpensesDto dto) {
        String[] attachedImagePaths = dto.getAttachedPaths();
        attachedService.addAttachedImages(referenceID, AttachedTypeEnum.ATTACHED_OF_EXPENSES, attachedImagePaths);
    }

    private ExpensesEntity createExpensesFromDto(ExpensesDto dto) {
        ExpensesEntity expenses = expensesMapper.toExpensesEntity(dto);
        expensesRepo.save(expenses);
        return expenses;
    }

    @Override
    public ExpensesDto update(String id, ExpensesDto dto) {
        checkPermission(type, PermissionKeyEnum.WRITE);

        ExpensesEntity expenses = expensesRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Chi phí cần cập nhật không tồn tại!"));
        checkUpdateConditions(id);

        updateExpensesFromDto(expenses, dto);

        Optional<ExpensesDto> result = expensesRepo.getByID(expenses.getId());
        return result.orElse(null);
    }

    private void checkUpdateConditions(String id) {
        ExpensesStatusEnum status = expensesRepo.getStatusByID(id);
        if (status == ExpensesStatusEnum.APPROVE) {
            throw new ConflictException("Chi phí đã được duyệt không thể chỉnh sửa!");
        }
    }

    private void updateExpensesFromDto(ExpensesEntity expenses, ExpensesDto dto) {
        expensesMapper.updateExpenses(expenses, dto);
        expensesRepo.save(expenses);
    }

    @Override
    public long deleteByID(String id) throws ServerException {
        checkPermission(type, PermissionKeyEnum.DELETE);
        checkDeleteConditions(id);
        return delete(id);
    }

    private void checkDeleteConditions(String id) {
        if (expensesRepo.countByID(id) == 0)
            throw new NotFoundException("Chi phí cần xóa không tồn tại!");
    }

    private long delete(String id) throws ServerException {
        long result = expensesRepo.delete(id);
        if (result == 0) {
            throw new ServerException("Có lỗi khi xóa chi phí. Vui lòng thử lại sau!");
        }
        return result;
    }

    @Override
    public long approveByID(String id) throws ServerException {
        checkPermission(type, PermissionKeyEnum.APPROVE);
        checkApproveConditions(id);
        return approve(id);
    }

    private void checkApproveConditions(String id) {
        if (expensesRepo.countByID(id) == 0)
            throw new NotFoundException("Chi phí cần duyệt không tồn tại!");

        ExpensesStatusEnum status = expensesRepo.getStatusByID(id);
        if (status == ExpensesStatusEnum.APPROVE) {
            throw new NotModifiedException("Chi phí đã được duyệt trước đó!");
        }
    }

    private long approve(String id) throws ServerException {
        long result = expensesRepo.approve(id);
        if (result == 0) {
            throw new ServerException("Có lỗi khi duyệt chi phí. Vui lòng thử lại sau!");
        }
        return result;
    }

    @Override
    public List<ExpensesIncurredDto> report(String driverId, String period) {
        checkPermission(PermissionTypeEnum.REPORTS, PermissionKeyEnum.VIEW);
        Date[] range = utils.createDateRange(period);
        return expensesRepo.getExpenseIncurredByDriverID(driverId, range[0], range[1]);
    }

    @Override
    public List<ExpensesReportDto> reportAll(String period) {
        checkPermission(PermissionTypeEnum.REPORTS, PermissionKeyEnum.VIEW);
        return expensesRepo.reportAll(period);
    }

    @Override
    public List<ExpensesEntity> importExpensesData(MultipartFile importFile) {

        checkPermission(type, PermissionKeyEnum.WRITE);

        Workbook workbook = FileFactory.getWorkbookStream(importFile);
        List<ExpensesDto> expensesDtoList = ExcelUtils.getImportData(workbook, ImportConfig.expensesImport);

        List<ExpensesEntity> expenses = expensesMapper.toExpensesEntities(expensesDtoList);

        return expensesRepo.saveAll(expenses);
    }

    @Override
    public ExportExcelResponse exportExpenses(String expensesConfigId, String truckLicense, String fromDate, String toDate) throws Exception {
        Date[] range = utils.createDateRange(fromDate, toDate);
        List<ExpensesDto> expenses = expensesRepo.getAll(expensesConfigId, truckLicense, range[0], range[1]);

        if (CollectionUtils.isEmpty(expenses)) {
            throw new NotFoundException("No data");
        }
        String fileName = "Expenses Export" + ".xlsx";

        ByteArrayInputStream in = ExcelUtils.export(expenses, fileName, ExportConfig.expensesExport);

        InputStreamResource inputStreamResource = new InputStreamResource(in);
        return new ExportExcelResponse(fileName, inputStreamResource);
    }

    @Override
    public ExportExcelResponse exportReportExpenses(String driverId, String period) throws Exception {
        Date[] range = utils.createDateRange(period);
        List<ExpensesIncurredDto> expensesReport = expensesRepo.getExpenseIncurredByDriverID(driverId, range[0], range[1]);

        if (CollectionUtils.isEmpty(expensesReport)) {
            throw new NotFoundException("No data");
        }
        String fileName = "ExpensesReport Export" + ".xlsx";

        ByteArrayInputStream in = ExcelUtils.export(expensesReport, fileName, ExportConfig.expenseReportByDriverExport);

        InputStreamResource inputStreamResource = new InputStreamResource(in);
        return new ExportExcelResponse(fileName, inputStreamResource);
    }
}
