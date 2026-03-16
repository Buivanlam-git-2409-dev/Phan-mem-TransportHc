package com.transporthc.repository.salary;

import com.transporthc.dto.salary.SalaryUserDto;
import com.transporthc.dto.salary.UpdateSalaryDto;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface SalaryRepoCustom {
    List<SalaryUserDto> getAllSalaryWithUserPeriod(String period);
    SalaryUserDto getSalaryWithUser(Integer id);
    Boolean updateSalary(Integer id, UpdateSalaryDto updateSalaryDTO);
    void createSalaryForAllUsers();
}
