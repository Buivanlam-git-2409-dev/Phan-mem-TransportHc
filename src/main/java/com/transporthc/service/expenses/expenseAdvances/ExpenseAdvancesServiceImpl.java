package com.transporthc.service.expenses.expenseAdvances;
import com.transporthc.dto.expenses.ExpensesAdvancesDto;
import com.transporthc.entity.expenses.ExpenseAdvances;
import com.transporthc.enums.permission.PermissionKeyEnum;
import com.transporthc.enums.permission.PermissionTypeEnum;
import com.transporthc.exception.define.NotFoundException;
import com.transporthc.mapper.expenses.ExpennsesAdvancesMapper;
import com.transporthc.repository.expenses.expenseadvances.ExpenseAdvanceRepo;
import com.transporthc.service.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseAdvancesServiceImpl extends BaseService implements ExpenseAdvancesService {
    private final ExpenseAdvanceRepo expenseAdvancesRepo;
    private final ExpennsesAdvancesMapper expenseAdvancesMapper;
    private final PermissionTypeEnum type = PermissionTypeEnum.EXPENSES;

    @Override
    public List<ExpensesAdvancesDto> getAll(int page) {
        checkPermission(type, PermissionKeyEnum.VIEW);
        return expenseAdvancesRepo.getAll(page);
    }

    @Override
    public ExpensesAdvancesDto getByDriverId(String id) {
        checkPermission(type, PermissionKeyEnum.VIEW);
        return expenseAdvancesRepo.getByDriverId(id)
                .orElseThrow(() -> new NotFoundException("Thông tin ứng chi phí không tồn tại!"));
    }

    @Override
    public long delete(Integer id) {
        checkPermission(type, PermissionKeyEnum.DELETE);

        return expenseAdvancesRepo.deleted(id);
    }

    @Override
    public ExpensesAdvancesDto update(Integer id, ExpensesAdvancesDto dto) {
        checkPermission(type, PermissionKeyEnum.WRITE);
        ExpensesAdvancesDto expenseAdvancesDto = expenseAdvancesRepo.getExpenseAdvanceById(id)
                .orElseThrow(() -> new NotFoundException("Thông tin ứng chi phí không tồn tại!"));
        ExpenseAdvances expenseAdvances = expenseAdvancesMapper.toExpenseAdvancesEntity(expenseAdvancesDto);
        expenseAdvancesMapper.updateExpenseAdvance(id, expenseAdvances, dto);
        expenseAdvancesRepo.save(expenseAdvances);
        return expenseAdvancesRepo.getExpenseAdvanceById(id).get();
    }

    @Override
    public ExpensesAdvancesDto create(ExpensesAdvancesDto dto) {
        checkPermission(type, PermissionKeyEnum.WRITE);
        ExpenseAdvances expenseAdvances = expenseAdvancesMapper.toExpenseAdvancesEntity(dto);
        return expenseAdvancesRepo.getExpenseAdvanceById(
                expenseAdvancesRepo.save(expenseAdvances).getId()
        ).get();
    }
}