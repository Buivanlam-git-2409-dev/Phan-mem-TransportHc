package com.transporthc.service.salary;

import java.util.List;

import org.springframework.stereotype.Service;

import com.transporthc.dto.salary.SalaryUserDto;
import com.transporthc.dto.salary.UpdateSalaryDto;

@Service
public interface SalaryService {
    List<SalaryUserDto> getAllSalaryWithUserPeriod(String period);
    SalaryUserDto getSalaryWithUser(Integer id);
    SalaryUserDto updateSalary(Integer id, UpdateSalaryDto updateSalaryDTO);
    void createSalaryForAllUsers();
}
