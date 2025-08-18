package com.transporthc.service.salary;

import com.transporthc.dto.salary.SalaryUserDto;
import com.transporthc.dto.salary.UpdateSalaryDto;
import com.transporthc.enums.permission.PermissionKeyEnum;
import com.transporthc.enums.permission.PermissionTypeEnum;
import com.transporthc.repository.salary.SalaryRepo;
import com.transporthc.service.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SalaryServiceImpl extends BaseService implements SalaryService {

    private final SalaryRepo salaryRepo;
    private final PermissionTypeEnum type = PermissionTypeEnum.SALARIES;

    @Override
    public List<SalaryUserDto> getAllSalaryWithUserPeriod(String period) {
        checkPermission(type, PermissionKeyEnum.VIEW);
        return salaryRepo.getAllSalaryWithUserPeriod(period);
    }

    @Override
    public SalaryUserDto getSalaryWithUser(Integer id) {
        checkPermission(type, PermissionKeyEnum.VIEW);
        return salaryRepo.getSalaryWithUser(id);
    }

    @Override
    public SalaryUserDto updateSalary(Integer id, UpdateSalaryDto updateSalaryDto) {

        checkPermission(type, PermissionKeyEnum.WRITE);

        Boolean updated = salaryRepo.updateSalary(id, updateSalaryDto);

        SalaryUserDto updatedSalary = null;
        if(updated){
            updatedSalary = salaryRepo.getSalaryWithUser(id);
        }

        return updatedSalary;
    }

    public void createSalaryForAllUsers(){
        salaryRepo.createSalaryForAllUsers();
    }
}